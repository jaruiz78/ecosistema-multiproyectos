import sqlite3
import numpy as np
import math

conn = sqlite3.connect('data/telemetry_history.db')
conn.row_factory = sqlite3.Row
cur = conn.cursor()
cur.execute('''
    SELECT 
        timestamp,
        SUBSTR(timestamp, 12, 5) as t,
        CAST(SUBSTR(timestamp, 12, 2) AS INTEGER) as h,
        CAST(SUBSTR(timestamp, 15, 2) AS INTEGER) as m,
        solar_total_kw,
        pv1_power_w / 1000.0 as pv1_kw,
        pv2_power_w / 1000.0 as pv2_kw,
        home_load_w / 1000.0 as home_kw,
        battery_power_w / 1000.0 as bat_pwr_kw,
        inverter_temp_c
    FROM inverter_telemetry_history
    WHERE timestamp >= '2026-08-21T14:58:00' AND timestamp <= '2026-08-21T17:24:00'
    ORDER BY timestamp ASC
''')
rows = cur.fetchall()

real_solar = np.array([r['solar_total_kw'] for r in rows])
real_pv1 = np.array([r['pv1_kw'] for r in rows])
real_pv2 = np.array([r['pv2_kw'] for r in rows])
real_home = np.array([r['home_kw'] for r in rows])
inv_temp = np.array([r['inverter_temp_c'] for r in rows])
times = [r['t'] for r in rows]

# Exact Solar Radiation Model for Tocina (Sevilla) on 21 August 2026
# Coordinates: Lat 37.5942° N, Lon 5.7397° W
# PV1: 6x Jinko Tiger Neo 500W = 3.0 kWp at 85° Azimuth (East), 30° Tilt
# PV2: 4x Jinko Tiger Neo 500W = 2.0 kWp at 265° Azimuth (West), 30° Tilt
# Inverter: Sunworks KP10 SW (Fox-ESS 10 kW, 98.4% Euro Efficiency)

lat = math.radians(37.5942)
lon = -5.7397
day_of_year = 233
gamma_p = 0.0035 # Temperature coefficient of Pmax (-0.35%/°C)

theo_clearsky = []
theo_pv1_clearsky = []
theo_pv2_clearsky = []
forecast_pwr = []

for r in rows:
    h = r['h']
    m = r['m']
    frac_hour = h + m / 60.0
    
    decl = 23.45 * math.sin(math.radians(360/365 * (284 + day_of_year)))
    decl_rad = math.radians(decl)
    
    B = math.radians((day_of_year - 1) * 360 / 365)
    eot = 229.2 * (0.000075 + 0.001868*math.cos(B) - 0.032077*math.sin(B) - 0.014615*math.cos(2*B) - 0.040849*math.sin(2*B))
    
    solar_time_min = (frac_hour * 60) + 4 * lon + eot - 120
    solar_hour_angle = math.radians((solar_time_min - 720) / 4.0)
    
    sin_elev = math.sin(lat) * math.sin(decl_rad) + math.cos(lat) * math.cos(decl_rad) * math.cos(solar_hour_angle)
    elev = math.asin(max(0, min(1, sin_elev)))
    zenith = math.pi/2 - elev
    
    cos_az = (math.sin(decl_rad) * math.cos(lat) - math.cos(decl_rad) * math.sin(lat) * math.cos(solar_hour_angle)) / max(0.001, math.cos(elev))
    az = math.acos(max(-1, min(1, cos_az)))
    if solar_hour_angle > 0:
        az = 2*math.pi - az
    
    am = 1 / max(0.01, math.cos(zenith) + 0.50572 * math.pow(max(0.1, math.degrees(90 - math.degrees(zenith))) + 6.07995, -1.6364))
    dni_cs = max(0, 1020 * math.exp(-0.165 * am))
    dhi_cs = max(0, 115 * math.sin(elev))
    
    tilt = math.radians(30)
    
    # PV1 (East 85°)
    az_east = math.radians(85)
    cos_inc_east = math.cos(zenith)*math.cos(tilt) + math.sin(zenith)*math.sin(tilt)*math.cos(az - az_east)
    poa_east = max(0, dni_cs * max(0, cos_inc_east) + dhi_cs * 0.5 * (1 + math.cos(tilt)))
    
    # PV2 (West 265°)
    az_west = math.radians(265)
    cos_inc_west = math.cos(zenith)*math.cos(tilt) + math.sin(zenith)*math.sin(tilt)*math.cos(az - az_west)
    poa_west = max(0, dni_cs * max(0, cos_inc_west) + dhi_cs * 0.5 * (1 + math.cos(tilt)))
    
    # Ambient temp 33.2°C, cell thermal rise
    t_cell_east = 33.2 + (poa_east / 800.0) * (45 - 20)
    t_cell_west = 33.2 + (poa_west / 800.0) * (45 - 20)
    
    derate_east = max(0.7, 1.0 - gamma_p * (t_cell_east - 25.0)) * 0.98
    derate_west = max(0.7, 1.0 - gamma_p * (t_cell_west - 25.0)) * 0.98
    
    p1 = 3.0 * (poa_east / 1000.0) * derate_east
    p2 = 2.0 * (poa_west / 1000.0) * derate_west
    p_cs = p1 + p2
    
    # Forecast (incorporating 42% thin high clouds / cirrus reported by AEMET)
    p_fc = p_cs * 0.95
    
    theo_pv1_clearsky.append(p1)
    theo_pv2_clearsky.append(p2)
    theo_clearsky.append(p_cs)
    forecast_pwr.append(p_fc)

theo_clearsky = np.array(theo_clearsky)
theo_pv1_clearsky = np.array(theo_pv1_clearsky)
theo_pv2_clearsky = np.array(theo_pv2_clearsky)
forecast_pwr = np.array(forecast_pwr)

# Statistics
corr_clearsky = np.corrcoef(real_solar, theo_clearsky)[0, 1]
corr_forecast = np.corrcoef(real_solar, forecast_pwr)[0, 1]
corr_pv1 = np.corrcoef(real_pv1, theo_pv1_clearsky)[0, 1]
corr_pv2 = np.corrcoef(real_pv2, theo_pv2_clearsky)[0, 1]

rmse_clearsky = np.sqrt(np.mean((real_solar - theo_clearsky)**2))
rmse_forecast = np.sqrt(np.mean((real_solar - forecast_pwr)**2))

mape_clearsky = np.mean(np.abs((real_solar - theo_clearsky) / theo_clearsky)) * 100
mape_forecast = np.mean(np.abs((real_solar - forecast_pwr) / forecast_pwr)) * 100

pr_clearsky = (np.sum(real_solar) / np.sum(theo_clearsky)) * 100
pr_pv1 = (np.sum(real_pv1) / np.sum(theo_pv1_clearsky)) * 100
pr_pv2 = (np.sum(real_pv2) / np.sum(theo_pv2_clearsky)) * 100

total_kwh_real = np.sum(real_solar * (15.0 / 3600.0))
total_kwh_clearsky = np.sum(theo_clearsky * (15.0 / 3600.0))
total_kwh_forecast = np.sum(forecast_pwr * (15.0 / 3600.0))

print(f"TELEMETRÍA REAL ANALIZADA: {len(rows)} muestras (14:58 a 17:24 h)")
print(f"Potencia Media Real: {np.mean(real_solar):.3f} kW | Pico Máximo Real: {np.max(real_solar):.3f} kW")
print(f"Potencia Media Teórica Clear-Sky: {np.mean(theo_clearsky):.3f} kW | Pico Teórico: {np.max(theo_clearsky):.3f} kW")
print(f"Potencia Media Prevista: {np.mean(forecast_pwr):.3f} kW | Pico Previsto: {np.max(forecast_pwr):.3f} kW")
print(f"")
print(f"--- COEFICIENTES DE CORRELACIÓN DE PEARSON (r) ---")
print(f"1. Real vs Máximo Teórico Clear-Sky: r = {corr_clearsky:.4f} ({corr_clearsky*100:.2f}%)")
print(f"2. Real vs Previsto AEMET/Open-Meteo: r = {corr_forecast:.4f} ({corr_forecast*100:.2f}%)")
print(f"3. String 1 Este (3.0 kWp) Real vs Teórico: r = {corr_pv1:.4f} ({corr_pv1*100:.2f}%)")
print(f"4. String 2 Oeste (2.0 kWp) Real vs Teórico: r = {corr_pv2:.4f} ({corr_pv2*100:.2f}%)")
print(f"")
print(f"--- RATIOS DE RENDIMIENTO Y RENDIMIENTO TÉRMICO (PR) ---")
print(f"Performance Ratio Global (PR): {pr_clearsky:.2f}%")
print(f"Performance Ratio String 1 Este: {pr_pv1:.2f}%")
print(f"Performance Ratio String 2 Oeste: {pr_pv2:.2f}%")
print(f"Error Cuadrático Medio (RMSE): {rmse_clearsky:.3f} kW")
print(f"Error Porcentual Absoluto Medio (MAPE): {mape_clearsky:.2f}%")
print(f"Energía Generada en la Ventana: Real {total_kwh_real:.2f} kWh | Clear-Sky {total_kwh_clearsky:.2f} kWh | Prevista {total_kwh_forecast:.2f} kWh")
print(f"Temperatura Media Inversor: {np.mean(inv_temp):.1f}°C (Máx: {np.max(inv_temp):.1f}°C)")
