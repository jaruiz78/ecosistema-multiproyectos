#!/usr/bin/env python3
"""
Ingestor y Gestor Universal del Histórico Climático ERA5 (5 Años: 2021-2026)
Consolida 49.344 horas de telemetría de radiación solar, temperatura, evapotranspiración ET0,
viento y nubosidad para Los Rosales / Tocina (Sevilla) y el ecosistema MultiProyectos.
"""

import os
import sys
import json
import sqlite3
import urllib.request
import urllib.parse
from datetime import datetime

BASE_DIR = "/home/jaruiz/Desarrollo"
DB_DIR = os.path.join(BASE_DIR, "data")
DB_PATH = os.path.join(DB_DIR, "weather_cache.db")
GLOBAL_DB_PATH = os.path.join(DB_DIR, "simulations_telemetry.db")

LATITUDE = 37.5942
LONGITUDE = -5.7397
START_DATE = "2021-01-01"
END_DATE = "2026-08-18"

def init_historical_schema(conn):
    conn.execute("""
        CREATE TABLE IF NOT EXISTS climate_5yr_reanalysis_hourly (
            timestamp TEXT PRIMARY KEY,
            epoch_seconds INTEGER,
            year INTEGER,
            month INTEGER,
            day INTEGER,
            hour INTEGER,
            temp_c REAL,
            relative_humidity_pct REAL,
            precipitation_mm REAL,
            cloud_cover_pct INTEGER,
            dni_w_m2 REAL,
            dhi_w_m2 REAL,
            ghi_w_m2 REAL,
            et0_fao_mm REAL,
            wind_speed_ms REAL,
            solar_pv_kw REAL,
            hvac_load_w REAL
        );
    """)
    conn.execute("CREATE INDEX IF NOT EXISTS idx_clim_year_month ON climate_5yr_reanalysis_hourly(year, month);")
    conn.execute("CREATE INDEX IF NOT EXISTS idx_clim_month ON climate_5yr_reanalysis_hourly(month);")
    conn.execute("CREATE INDEX IF NOT EXISTS idx_clim_epoch ON climate_5yr_reanalysis_hourly(epoch_seconds);")

    # Vista analítica mensual agregada
    conn.execute("""
        CREATE VIEW IF NOT EXISTS v_monthly_climate_summary AS
        SELECT 
            year,
            month,
            COUNT(*) as hours_count,
            ROUND(AVG(temp_c), 2) as avg_temp_c,
            ROUND(MIN(temp_c), 2) as min_temp_c,
            ROUND(MAX(temp_c), 2) as max_temp_c,
            ROUND(SUM(precipitation_mm), 2) as total_precip_mm,
            ROUND(SUM(et0_fao_mm), 2) as total_et0_mm,
            ROUND(AVG(cloud_cover_pct), 1) as avg_cloud_cover_pct,
            ROUND(AVG(ghi_w_m2), 1) as avg_ghi_w_m2,
            ROUND(MAX(ghi_w_m2), 1) as max_ghi_w_m2,
            ROUND(SUM(solar_pv_kw), 2) as total_solar_kwh_5kwp,
            ROUND(SUM(hvac_load_w) / 1000.0, 2) as total_hvac_kwh
        FROM climate_5yr_reanalysis_hourly
        GROUP BY year, month
        ORDER BY year, month;
    """)

    # Vista analítica anual
    conn.execute("""
        CREATE VIEW IF NOT EXISTS v_annual_climate_summary AS
        SELECT 
            year,
            ROUND(AVG(temp_c), 2) as avg_temp_c,
            ROUND(MIN(temp_c), 2) as min_temp_c,
            ROUND(MAX(temp_c), 2) as max_temp_c,
            ROUND(SUM(precipitation_mm), 2) as total_precip_mm,
            ROUND(SUM(et0_fao_mm), 2) as total_et0_mm,
            ROUND(SUM(solar_pv_kw), 2) as total_solar_kwh_5kwp,
            ROUND(SUM(solar_pv_kw) / 365.0, 2) as avg_daily_solar_kwh
        FROM climate_5yr_reanalysis_hourly
        GROUP BY year
        ORDER BY year;
    """)
    conn.commit()

def calculate_solar_and_hvac(hour, temp_c, dni, dhi, ghi, cloud_cover):
    """Modelo físico rápido O(1) de generación solar (5 kWp Jinko) y demanda Daikin"""
    # 1. Generación Solar 10x Jinko 500W (3 kWp Este + 2 kWp Oeste)
    solar_kw = 0.0
    if ghi > 5 and hour >= 6 and hour <= 21:
        # Orientación E/W ponderada
        optical_yield = 0.98 * max(0.1, 1.0 - (cloud_cover / 100.0) * 0.75)
        temp_loss = 1.0 + (-0.0030) * max(0, temp_c - 25.0)
        # 5.0 kWp nominal
        solar_dc_w = 5.0 * ghi * optical_yield * max(0.70, temp_loss)
        solar_kw = round(min(10.0, (solar_dc_w / 1000.0) * 0.98), 3)

    # 2. Demanda climatización Daikin (Salón 35m² + Dormitorio 16m²)
    hvac_w = 0.0
    if hour >= 13 and hour <= 23:
        if temp_c > 27.0: # Refrigeración
            hvac_w += min(950, 350 + (temp_c - 24.0) * 38.0)
        elif temp_c < 15.0: # Bomba de calor
            hvac_w += min(850, 300 + (21.0 - temp_c) * 32.0)

    if (hour >= 15 and hour <= 17) or hour >= 23 or hour <= 7:
        if temp_c > 27.0:
            hvac_w += min(650, 220 + (temp_c - 24.0) * 25.0)
        elif temp_c < 15.0:
            hvac_w += min(550, 180 + (20.0 - temp_c) * 22.0)

    return solar_kw, round(hvac_w, 1)

def ingest_5yr_data():
    print(f"🌍 [ERA5 Ingestion] Descargando histórico 5 años ({START_DATE} -> {END_DATE}) para Tocina ({LATITUDE}, {LONGITUDE})...")
    
    params = urllib.parse.urlencode({
        "latitude": str(LATITUDE),
        "longitude": str(LONGITUDE),
        "start_date": START_DATE,
        "end_date": END_DATE,
        "hourly": ",".join([
            "temperature_2m",
            "relative_humidity_2m",
            "precipitation",
            "cloud_cover",
            "direct_normal_irradiance",
            "diffuse_radiation",
            "shortwave_radiation",
            "et0_fao_evapotranspiration",
            "wind_speed_10m"
        ]),
        "timezone": "Europe/Madrid"
    })
    
    url = f"https://archive-api.open-meteo.com/v1/archive?{params}"
    req = urllib.request.Request(url, headers={"User-Agent": "MultiProyectos-UniversalClimate/2.0"})
    
    with urllib.request.urlopen(req, timeout=45.0) as resp:
        data = json.loads(resp.read().decode('utf-8'))
        
    hourly = data.get("hourly", {})
    times = hourly.get("time", [])
    total_hours = len(times)
    print(f"📥 Datos recibidos: {total_hours:,} registros horarios. Procesando e insertando en SQLite...")

    temps = hourly.get("temperature_2m", [])
    hum = hourly.get("relative_humidity_2m", [])
    precip = hourly.get("precipitation", [])
    clouds = hourly.get("cloud_cover", [])
    dnis = hourly.get("direct_normal_irradiance", [])
    dhis = hourly.get("diffuse_radiation", [])
    ghis = hourly.get("shortwave_radiation", [])
    et0s = hourly.get("et0_fao_evapotranspiration", [])
    winds = hourly.get("wind_speed_10m", [])

    records = []
    for i in range(total_hours):
        t_str = times[i]
        dt = datetime.fromisoformat(t_str)
        epoch = int(dt.timestamp())
        
        tc = temps[i] if temps[i] is not None else 20.0
        rh = hum[i] if hum[i] is not None else 50.0
        pr = precip[i] if precip[i] is not None else 0.0
        cc = int(clouds[i]) if clouds[i] is not None else 0
        dni = dnis[i] if dnis[i] is not None else 0.0
        dhi = dhis[i] if dhis[i] is not None else 0.0
        ghi = ghis[i] if ghis[i] is not None else 0.0
        et0 = et0s[i] if et0s[i] is not None else 0.0
        wind = winds[i] if winds[i] is not None else 0.0

        solar_kw, hvac_w = calculate_solar_and_hvac(dt.hour, tc, dni, dhi, ghi, cc)

        records.append((
            t_str, epoch, dt.year, dt.month, dt.day, dt.hour,
            tc, rh, pr, cc, dni, dhi, ghi, et0, wind,
            solar_kw, hvac_w
        ))

    # Guardar en local weather_cache.db y en simulations_telemetry.db
    for target_db in [DB_PATH, os.path.join(BASE_DIR, "apps", "ProyectoSolarTocina", "data", "weather_cache.db")]:
        os.makedirs(os.path.dirname(target_db), exist_ok=True)
        with sqlite3.connect(target_db) as conn:
            init_historical_schema(conn)
            conn.executemany("""
                INSERT OR REPLACE INTO climate_5yr_reanalysis_hourly (
                    timestamp, epoch_seconds, year, month, day, hour,
                    temp_c, relative_humidity_pct, precipitation_mm, cloud_cover_pct,
                    dni_w_m2, dhi_w_m2, ghi_w_m2, et0_fao_mm, wind_speed_ms,
                    solar_pv_kw, hvac_load_w
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, records)
            conn.commit()

    # Guardar resumen de conocimiento climático en simulations_telemetry.db para RAG e IA
    with sqlite3.connect(GLOBAL_DB_PATH) as conn:
        conn.execute("""
            CREATE TABLE IF NOT EXISTS climate_knowledge_nodes (
                year INTEGER,
                month INTEGER,
                avg_temp_c REAL,
                total_precip_mm REAL,
                total_et0_mm REAL,
                total_solar_kwh REAL,
                peak_temp_c REAL,
                insights_json TEXT,
                updated_at TIMESTAMP,
                PRIMARY KEY(year, month)
            )
        """)
        
        # Calcular agregados
        with sqlite3.connect(DB_PATH) as lconn:
            cur = lconn.cursor()
            cur.execute("SELECT year, month, avg_temp_c, total_precip_mm, total_et0_mm, total_solar_kwh_5kwp, max_temp_c FROM v_monthly_climate_summary")
            monthly_rows = cur.fetchall()
            
        for row in monthly_rows:
            y, m, avg_t, p, et, sol, max_t = row
            insights = {
                "location": "Tocina, Sevilla",
                "h3_index": "8939023447bffff",
                "season": "Verano" if m in [6,7,8] else ("Primavera" if m in [3,4,5] else ("Otoño" if m in [9,10,11] else "Invierno")),
                "solar_daily_avg_kwh": round(sol / 30.0, 2),
                "irrigation_need_mm": round(max(0, et - p), 2)
            }
            conn.execute("""
                INSERT OR REPLACE INTO climate_knowledge_nodes
                (year, month, avg_temp_c, total_precip_mm, total_et0_mm, total_solar_kwh, peak_temp_c, insights_json, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (y, m, avg_t, p, et, sol, max_t, json.dumps(insights), datetime.utcnow().isoformat()))
        conn.commit()

    print(f"✅ Ingesta Finalizada: {len(records):,} registros horarios consolidados en SQLite y Knowledge Graph.")

if __name__ == "__main__":
    ingest_5yr_data()
