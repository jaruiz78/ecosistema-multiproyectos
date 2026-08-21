import math
import numpy as np

# Coordenadas Tocina
lat = math.radians(37.5942)
lon = -5.7397

# Configuración 5.0 kWp
# String 1: 6x Jinko 500W = 3.0 kWp a 85° (Este), 30° inclinación
# String 2: 4x Jinko 500W = 2.0 kWp a 265° (Oeste), 30° inclinación

days_in_month = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
month_names = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]

# Factores medios de heliofanía / cobertura nubosa AEMET Sevilla (Vega del Guadalquivir)
# % de sol real vs 100% despejado
clearness_factors = [
    0.68, # Enero (nieblas/lluvias)
    0.74, # Feb
    0.79, # Mar (frentes de primavera)
    0.82, # Abr
    0.88, # May
    0.95, # Jun (casi 100% despejado)
    0.98, # Jul (cielo azul continuo)
    0.97, # Ago
    0.89, # Sep
    0.78, # Oct (borrascas otoño)
    0.71, # Nov
    0.66  # Dic (mes más nuboso)
]

# Temperaturas medias diurnas Tocina (°C)
temp_diurnal_avg = [15.5, 17.2, 20.8, 23.5, 28.2, 33.5, 37.8, 37.2, 31.5, 25.4, 19.2, 16.0]

gamma_p = 0.0029 # Jinko TOPCon

clearsky_monthly_5kw = []
real_forecast_monthly_5kw = []

clearsky_monthly_6kw = []
real_forecast_monthly_6kw = []

day_of_year_mid = [15, 45, 74, 105, 135, 166, 196, 227, 258, 288, 319, 349]

for m_idx in range(12):
    doy = day_of_year_mid[m_idx]
    n_days = days_in_month[m_idx]
    cf = clearness_factors[m_idx]
    t_amb = temp_diurnal_avg[m_idx]
    
    decl = 23.45 * math.sin(math.radians(360/365 * (284 + doy)))
    decl_rad = math.radians(decl)
    
    daily_cs_5kw = 0.0
    daily_cs_6kw = 0.0
    
    # Integrar día en pasos de 15 min
    for step in range(96):
        frac_h = step / 4.0
        # Ángulo horario solar
        solar_hour_angle = math.radians((frac_h - 12.0) * 15.0)
        
        sin_elev = math.sin(lat) * math.sin(decl_rad) + math.cos(lat) * math.cos(decl_rad) * math.cos(solar_hour_angle)
        if sin_elev <= 0:
            continue
            
        elev = math.asin(sin_elev)
        zenith = math.pi/2 - elev
        
        cos_az = (math.sin(decl_rad) * math.cos(lat) - math.cos(decl_rad) * math.sin(lat) * math.cos(solar_hour_angle)) / max(0.001, math.cos(elev))
        az = math.acos(max(-1, min(1, cos_az)))
        if solar_hour_angle > 0:
            az = 2*math.pi - az
            
        am = 1 / max(0.01, math.cos(zenith) + 0.50572 * math.pow(max(0.1, math.degrees(90 - math.degrees(zenith))) + 6.07995, -1.6364))
        dni = 1040 * math.exp(-0.16 * am)
        dhi = 115 * math.sin(elev)
        
        tilt = math.radians(30)
        
        # Este (85°)
        az_east = math.radians(85)
        cos_inc_e = math.cos(zenith)*math.cos(tilt) + math.sin(zenith)*math.sin(tilt)*math.cos(az - az_east)
        poa_e = max(0, dni * max(0, cos_inc_e) + dhi * 0.5 * (1 + math.cos(tilt)))
        
        # Oeste (265°)
        az_west = math.radians(265)
        cos_inc_w = math.cos(zenith)*math.cos(tilt) + math.sin(zenith)*math.sin(tilt)*math.cos(az - az_west)
        poa_w = max(0, dni * max(0, cos_inc_w) + dhi * 0.5 * (1 + math.cos(tilt)))
        
        t_cell_e = t_amb + (poa_e / 800.0) * (45 - 20)
        t_cell_w = t_amb + (poa_w / 800.0) * (45 - 20)
        
        derate_e = max(0.7, 1.0 - gamma_p * (t_cell_e - 25.0)) * 0.98
        derate_w = max(0.7, 1.0 - gamma_p * (t_cell_w - 25.0)) * 0.98
        
        # 5.0 kWp (3kW Este + 2kW Oeste)
        p_5kw = (3.0 * (poa_e / 1000.0) * derate_e) + (2.0 * (poa_w / 1000.0) * derate_w)
        # 6.0 kWp (3kW Este + 3kW Oeste)
        p_6kw = (3.0 * (poa_e / 1000.0) * derate_e) + (3.0 * (poa_w / 1000.0) * derate_w)
        
        daily_cs_5kw += p_5kw * 0.25 # 15 min = 0.25h
        daily_cs_6kw += p_6kw * 0.25
        
    m_cs_5kw = daily_cs_5kw * n_days
    m_real_5kw = m_cs_5kw * cf
    
    m_cs_6kw = daily_cs_6kw * n_days
    m_real_6kw = m_cs_6kw * cf
    
    clearsky_monthly_5kw.append(m_cs_5kw)
    real_forecast_monthly_5kw.append(m_real_5kw)
    
    clearsky_monthly_6kw.append(m_cs_6kw)
    real_forecast_monthly_6kw.append(m_real_6kw)

print("=== BALANCE MENSUAL: TEÓRICO MÁXIMO (CLEAR-SKY) vs REAL PREVISTO (TOCINA) ===")
print(f"{'Mes':<12} | {'5.0 kWp Clear-Sky':<18} | {'5.0 kWp Real Previsto':<22} | {'% Pérdida Clima':<16}")
print("-" * 75)
for i in range(12):
    cs = clearsky_monthly_5kw[i]
    rf = real_forecast_monthly_5kw[i]
    loss_pct = (1.0 - rf/cs) * 100.0
    print(f"{month_names[i]:<12} | {cs:8.1f} kWh        | {rf:8.1f} kWh            | {loss_pct:5.1f} %")

tot_cs_5 = sum(clearsky_monthly_5kw)
tot_rf_5 = sum(real_forecast_monthly_5kw)
tot_cs_6 = sum(clearsky_monthly_6kw)
tot_rf_6 = sum(real_forecast_monthly_6kw)

print("-" * 75)
print(f"TOTAL ANUAL (5.0 kWp): Máximo Teórico Clear-Sky = {tot_cs_5:.1f} kWh | Real Previsto Tocina = {tot_rf_5:.1f} kWh (Diferencia: -{(1-tot_rf_5/tot_cs_5)*100:.1f}%)")
print(f"TOTAL ANUAL (6.0 kWp): Máximo Teórico Clear-Sky = {tot_cs_6:.1f} kWh | Real Previsto Tocina = {tot_rf_6:.1f} kWh (Diferencia: -{(1-tot_rf_6/tot_cs_6)*100:.1f}%)")
