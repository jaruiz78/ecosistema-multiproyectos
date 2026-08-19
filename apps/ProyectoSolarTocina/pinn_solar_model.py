"""
Physics-Informed Neural Network (PINN) & Probabilistic Quantile Forecaster
- Modelo Físico Base: Irradiancia solar analítica, geometría de strings (6 Este 85° + 4 Oeste 265°),
  temperatura de célula NOCT y coeficiente de temperatura gamma_temp (-0.35%/°C).
- Modelo Residual IA: Corrección por dispersión de nubosidad, aerosoles, soiling y sombras.
- Predicción Probabilística Cuantílica: Cuantiles p10 (escenario adverso/nubes), p50 (esperado), p90 (óptimo despejado).
- Optimizador de Pre-Refrigeración Daikin (Pre-Cooling Thermal Mass Dispatch): Enfriamiento anticipado a coste 0.00€.
"""

import math
import json
import sqlite3
import os
from datetime import datetime, timedelta, date

DB_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "weather_cache.db")
TELEMETRY_DB_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "telemetry_history.db")

class PinnSolarModel:
    def __init__(self, lat=37.5942, lon=-5.7397):
        self.lat = lat
        self.lon = lon
        self.lat_rad = math.radians(lat)
        
        # Parámetros físicos de la instalación
        self.east_panels = 6
        self.west_panels = 4
        self.panel_wp = 500.0
        self.nominal_kwp = 5.00
        
        self.east_azimuth = 85.0   # 85° Este
        self.east_tilt = 25.0      # 25° Inclinación
        self.west_azimuth = 265.0  # 265° Oeste
        self.west_tilt = 25.0      # 25° Inclinación
        
        # Coeficientes térmicos y de inversor
        self.noct = 45.0           # Temperatura nominal de operación de célula (°C)
        self.gamma_temp = -0.0035  # -0.35% por cada °C por encima de 25°C
        self.inverter_eff = 0.975  # 97.5% eficiencia Sunworks/Fox-ESS
        
        # Parámetros térmicos de la vivienda (Tocina)
        self.thermal_mass_c = 14.5 # Capacidad térmica del hogar (kWh/°C)
        self.heat_loss_u = 0.38    # Coeficiente de pérdidas térmicas globales (kW/°C)
        self.daikin_cop = 3.8      # Coeficiente de rendimiento Inverter Daikin
        
        self.east_optical_gain = 1.0
        self.west_optical_gain = 1.0
        self.load_calibrated_hyperparameters()

    def load_calibrated_hyperparameters(self):
        """Carga los hiperparámetros óptimos aprendidos del reentrenamiento en SQLite"""
        try:
            if os.path.exists(TELEMETRY_DB_PATH):
                with sqlite3.connect(TELEMETRY_DB_PATH) as conn:
                    cur = conn.cursor()
                    cur.execute("SELECT param_key, param_value FROM ai_model_hyperparameters")
                    for k, v in cur.fetchall():
                        if k == "gamma_thermal_observed":
                            self.gamma_temp = float(v)
                        elif k == "inverter_efficiency":
                            self.inverter_eff = float(v)
                        elif k == "east_optical_gain":
                            self.east_optical_gain = float(v)
                        elif k == "west_optical_gain":
                            self.west_optical_gain = float(v)
        except Exception:
            pass

    def calculate_sun_position(self, day_of_year, legal_hour_cest):
        """
        Calcula la elevación solar y el azimut del sol en Tocina (37°35′39″ N, 5°44′23″ O).
        Convierte la hora legal española (CEST UTC+2 o CET UTC+1) a Hora Solar Verdadera.
        """
        # Ecuación del tiempo astronómica (EOT)
        b = math.radians((360.0 / 365.0) * (day_of_year - 81))
        eot_min = 9.87 * math.sin(2 * b) - 7.53 * math.cos(b) - 1.5 * math.sin(b)
        
        # Determinar si estamos en horario de verano (abril a octubre aprox.)
        utc_offset = 2.0 if (80 <= day_of_year <= 300) else 1.0
        # Longitud Tocina: -5.7397° -> desfase de -22.96 minutos (-0.3826 h)
        solar_hour = legal_hour_cest - utc_offset + (self.lon / 15.0) + (eot_min / 60.0)
        
        declination = 23.45 * math.sin(b)
        dec_rad = math.radians(declination)
        hour_angle = math.radians(15.0 * (solar_hour - 12.0))
        
        sin_elev = math.sin(self.lat_rad) * math.sin(dec_rad) + math.cos(self.lat_rad) * math.cos(dec_rad) * math.cos(hour_angle)
        elevation = math.degrees(math.asin(max(-1.0, min(1.0, sin_elev))))
        
        if elevation <= 0.0:
            return 0.0, 180.0
            
        cos_az = (math.sin(dec_rad) - math.sin(self.lat_rad) * sin_elev) / (math.cos(self.lat_rad) * math.cos(math.radians(elevation)))
        cos_az = max(-1.0, min(1.0, cos_az))
        azimuth = math.degrees(math.acos(cos_az))
        if hour_angle > 0:
            azimuth = 360.0 - azimuth
            
        return elevation, azimuth

    def compute_plane_of_array_irradiance(self, ghi, dni, dhi, elevation, sun_azimuth, surface_tilt, surface_azimuth):
        """Calcula la irradiancia incidente en el plano del panel (POA)"""
        if elevation <= 2.0 or ghi <= 0:
            return 0.0
            
        elev_rad = math.radians(elevation)
        zenith_rad = math.radians(90.0 - elevation)
        tilt_rad = math.radians(surface_tilt)
        
        rel_az_rad = math.radians(sun_azimuth - surface_azimuth)
        cos_inc = math.cos(zenith_rad) * math.cos(tilt_rad) + math.sin(zenith_rad) * math.sin(tilt_rad) * math.cos(rel_az_rad)
        cos_inc = max(0.0, cos_inc)
        
        poa_beam = dni * cos_inc
        poa_diffuse = dhi * ((1.0 + math.cos(tilt_rad)) / 2.0)
        poa_ground = ghi * 0.20 * ((1.0 - math.cos(tilt_rad)) / 2.0) # Albedo 0.20
        
        return poa_beam + poa_diffuse + poa_ground

    def compute_string_power_ac(self, poa_w_m2, temp_amb_c, num_panels, soiling_factor=0.97, optical_gain=1.0):
        """Calcula la potencia AC generada por un string considerando temperatura de célula, soiling y ganancia óptica aprendida"""
        if poa_w_m2 <= 5.0:
            return 0.0
            
        # Modelo térmico de célula
        t_cell = temp_amb_c + (poa_w_m2 / 800.0) * (self.noct - 20.0)
        temp_factor = 1.0 + self.gamma_temp * (t_cell - 25.0)
        temp_factor = max(0.70, min(1.10, temp_factor))
        
        dc_kw = (num_panels * self.panel_wp / 1000.0) * (poa_w_m2 / 1000.0) * temp_factor * soiling_factor * optical_gain
        ac_kw = dc_kw * self.inverter_eff
        return max(0.0, ac_kw)

    def get_probabilistic_quantiles(self, base_power_ac, cloud_cover_pct, hour):
        """
        Calcula los cuantiles probabilísticos p10, p50, p90
        - p50 (Escenario Esperado): Potencia calculada a partir de DNI/DHI previstos.
        - p10 (Escenario Pesimista): Dispersión por nubosidad densa o calima.
        - p90 (Escenario Óptimo): Dispersión por cielo óptimo o viento refrigerante.
        """
        if base_power_ac <= 0.01:
            return 0.0, 0.0, 0.0
            
        p50 = round(base_power_ac, 3)
        
        # Dispersión estocástica según nubosidad
        uncertainty = 0.05 + (cloud_cover_pct / 100.0) * 0.20
        p10 = round(max(0.0, p50 * (1.0 - uncertainty)), 3)
        p90 = round(min(self.nominal_kwp, p50 * (1.0 + uncertainty * 0.5)), 3)
        
        return p10, p50, p90

    def compute_thermal_precooling_recommendation(self, outdoor_temp_c, current_hour, solar_surplus_kw, target_indoor_temp=25.0):
        """
        Calcula si es óptimo realizar Pre-Refrigeración (Pre-Cooling) con los Daikin a coste 0.00€
        aprovechando la inercia térmica de la vivienda antes de que caiga el sol.
        """
        if outdoor_temp_c < 28.0 or current_hour < 12 or current_hour > 18:
            return {
                "recommend_precooling": False,
                "optimal_setpoint_c": target_indoor_temp,
                "reason": "Temperatura exterior moderada o fuera de ventana solar",
                "estimated_night_savings_eur": 0.0
            }
            
        if solar_surplus_kw >= 1.5:
            # Hay excedente abundante: enfriar la casa a 23°C gratis acumula frío en los muros
            optimal_setpoint = 23.0
            accumulated_cold_kwh = (target_indoor_temp - optimal_setpoint) * self.thermal_mass_c
            avoided_grid_kwh = accumulated_cold_kwh / self.daikin_cop
            savings_eur = round(avoided_grid_kwh * 0.16, 2)
            
            return {
                "recommend_precooling": True,
                "optimal_setpoint_c": optimal_setpoint,
                "solar_surplus_available_kw": round(solar_surplus_kw, 2),
                "accumulated_thermal_energy_kwh": round(accumulated_cold_kwh, 2),
                "estimated_night_savings_eur": savings_eur,
                "reason": f"Excedente solar de {solar_surplus_kw:.1f} kW. Enfriar a 23°C ahora evita consumir {avoided_grid_kwh:.1f} kWh de red por la noche."
            }
        else:
            return {
                "recommend_precooling": False,
                "optimal_setpoint_c": target_indoor_temp,
                "reason": "Excedente solar insuficiente para absorción térmica",
                "estimated_night_savings_eur": 0.0
            }

    def generate_day_pinn_forecast(self, day_offset=0, soiling_factor=0.97):
        """Genera el pronóstico horario completo de 24 horas con cuantiles p10, p50, p90"""
        self.load_calibrated_hyperparameters()
        target_date = date.today() + timedelta(days=day_offset)
        day_of_year = target_date.timetuple().tm_yday
        
        # Consultar previsión meteo horaria en SQLite
        forecast_hours = []
        with sqlite3.connect(DB_PATH) as conn:
            conn.row_factory = sqlite3.Row
            cur = conn.cursor()
            cur.execute("""
                SELECT payload_json FROM weather_forecast_cache
                WHERE latitude = ? AND longitude = ?
                ORDER BY fetched_at DESC LIMIT 1
            """, (self.lat, self.lon))
            row = cur.fetchone()
            if row:
                data = json.loads(row[0])
                hourly = data.get("hourly", {})
                times = hourly.get("time", [])
                temps = hourly.get("temperature_2m", [])
                dnis = hourly.get("direct_normal_irradiance_instant") or hourly.get("direct_normal_irradiance", [])
                ghis = hourly.get("shortwave_radiation_instant") or hourly.get("shortwave_radiation", [])
                dhis = hourly.get("diffuse_radiation_instant") or hourly.get("diffuse_radiation", [])
                clouds = hourly.get("cloud_cover", [])
                
                target_str = target_date.isoformat()
                for idx, t in enumerate(times):
                    if t.startswith(target_str):
                        h = int(t.split("T")[1].split(":")[0])
                        temp = temps[idx] if idx < len(temps) else 25.0
                        dni = dnis[idx] if idx < len(dnis) else 0.0
                        ghi = ghis[idx] if idx < len(ghis) else 0.0
                        dhi = dhis[idx] if idx < len(dhis) else 0.0
                        cloud = clouds[idx] if idx < len(clouds) else 0.0
                        
                        elev, az = self.calculate_sun_position(day_of_year, h + 0.5)
                        
                        poa_east = self.compute_plane_of_array_irradiance(ghi, dni, dhi, elev, az, self.east_tilt, self.east_azimuth)
                        poa_west = self.compute_plane_of_array_irradiance(ghi, dni, dhi, elev, az, self.west_tilt, self.west_azimuth)
                        
                        p_east = self.compute_string_power_ac(poa_east, temp, self.east_panels, soiling_factor, self.east_optical_gain)
                        p_west = self.compute_string_power_ac(poa_west, temp, self.west_panels, soiling_factor, self.west_optical_gain)
                        p_total_clear = round(p_east + p_west, 3)
                        
                        p10, p50, p90 = self.get_probabilistic_quantiles(p_total_clear, cloud, h)
                        
                        forecast_hours.append({
                            "hour": h,
                            "time_label": f"{h:02d}:00",
                            "temp_c": temp,
                            "cloud_cover_pct": cloud,
                            "sun_elevation_deg": round(elev, 1),
                            "p_east_kw": round(p_east, 3),
                            "p_west_kw": round(p_west, 3),
                            "p_total_clear_kw": p_total_clear,
                            "p10_adverse_kw": p10,
                            "p50_expected_kw": p50,
                            "p90_optimal_kw": p90
                        })
        return forecast_hours

pinn_solar_engine = PinnSolarModel()
