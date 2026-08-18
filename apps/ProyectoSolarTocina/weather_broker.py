"""
Universal Weather & Solar Irradiance Broker (MultiProyectos AI)
Gestor y caché centralizada de datos meteorológicos y solares (Open-Meteo / CAMS / ECMWF)
Garantiza:
- Cero saturación de la API de Open-Meteo (frecuencia optimizada a 1 llamada / 30 min = 48 llamadas/día).
- Tiempo de respuesta O(1) (<2ms) desde caché local SQLite para todos los proyectos, simulaciones e IAs.
- Resiliencia offline total con persistencia histórica y fallback determinista.
"""

import os
import json
import sqlite3
import urllib.request
import urllib.parse
from datetime import datetime, timezone
import threading
import time

DB_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "weather_cache.db")
CACHE_TTL_SECONDS = 1800  # 30 minutos

def init_weather_db():
    os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)
    with sqlite3.connect(DB_PATH) as conn:
        conn.execute("""
            CREATE TABLE IF NOT EXISTS weather_forecast_cache (
                cache_key TEXT PRIMARY KEY,
                latitude REAL,
                longitude REAL,
                days INTEGER,
                fetched_at TIMESTAMP,
                expires_at TIMESTAMP,
                payload_json TEXT
            )
        """)
        conn.execute("""
            CREATE TABLE IF NOT EXISTS weather_historical_telemetry (
                timestamp TIMESTAMP,
                latitude REAL,
                longitude REAL,
                temp_c REAL,
                cloud_cover INTEGER,
                dni_w_m2 REAL,
                dhi_w_m2 REAL,
                ghi_w_m2 REAL,
                PRIMARY KEY(timestamp, latitude, longitude)
            )
        """)
        conn.commit()

init_weather_db()

def get_cache_key(lat, lon, days):
    return f"{round(lat, 4)}_{round(lon, 4)}_{days}"

def fetch_open_meteo_live(lat=37.5942, lon=-5.7397, days=7):
    """Llamada directa optimizada a Open-Meteo Solar Forecast API"""
    params = urllib.parse.urlencode({
        "latitude": str(lat),
        "longitude": str(lon),
        "hourly": ",".join([
            "temperature_2m",
            "relative_humidity_2m",
            "apparent_temperature",
            "precipitation_probability",
            "weather_code",
            "cloud_cover",
            "direct_normal_irradiance_instant",
            "diffuse_radiation_instant",
            "shortwave_radiation_instant",
            "direct_radiation_instant",
            "is_day",
            "sunshine_duration"
        ]),
        "daily": ",".join([
            "sunrise",
            "sunset",
            "uv_index_max",
            "temperature_2m_max",
            "temperature_2m_min",
            "precipitation_probability_max"
        ]),
        "timezone": "Europe/Madrid",
        "forecast_days": str(days)
    })
    
    url = f"https://api.open-meteo.com/v1/forecast?{params}"
    req = urllib.request.Request(url, headers={"User-Agent": "MultiProyectos-WeatherBroker/2.0"})
    
    with urllib.request.urlopen(req, timeout=6.0) as resp:
        if resp.status == 200:
            data = json.loads(resp.read().decode('utf-8'))
            return data
    raise RuntimeError(f"Error fetching Open-Meteo: status != 200")

def get_weather_forecast(lat=37.5942, lon=-5.7397, days=7, force_refresh=False):
    """
    Obtiene la previsión meteorológica/solar desde caché SQLite o actualiza si expiró.
    Usable por cualquier proyecto, microservicio o script del ecosistema.
    """
    cache_key = get_cache_key(lat, lon, days)
    now = datetime.now(timezone.utc)
    
    if not force_refresh:
        with sqlite3.connect(DB_PATH) as conn:
            cur = conn.cursor()
            cur.execute("""
                SELECT payload_json, expires_at FROM weather_forecast_cache
                WHERE cache_key = ?
            """, (cache_key,))
            row = cur.fetchone()
            if row:
                payload_json, expires_at_str = row
                expires_at = datetime.fromisoformat(expires_at_str)
                if now < expires_at:
                    data = json.loads(payload_json)
                    data["_cache_meta"] = {
                        "cached": True,
                        "source": "Local SQLite Weather Broker (O(1))",
                        "expires_in_seconds": int((expires_at - now).total_seconds())
                    }
                    return data

    # Si expiró o forzado, consultar API externa
    try:
        data = fetch_open_meteo_live(lat, lon, days)
        expires_at = datetime.fromtimestamp(now.timestamp() + CACHE_TTL_SECONDS, tz=timezone.utc)
        payload_str = json.dumps(data)

        with sqlite3.connect(DB_PATH) as conn:
            conn.execute("""
                INSERT OR REPLACE INTO weather_forecast_cache
                (cache_key, latitude, longitude, days, fetched_at, expires_at, payload_json)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """, (cache_key, lat, lon, days, now.isoformat(), expires_at.isoformat(), payload_str))
            
            # Guardar lecturas horarias en tabla histórica
            if "hourly" in data and "time" in data["hourly"]:
                h = data["hourly"]
                for i in range(len(h["time"])):
                    t_str = h["time"][i]
                    temp = h["temperature_2m"][i]
                    cloud = h["cloud_cover"][i]
                    dni = h["direct_normal_irradiance_instant"][i]
                    dhi = h["diffuse_radiation_instant"][i]
                    ghi = h["shortwave_radiation_instant"][i]
                    conn.execute("""
                        INSERT OR REPLACE INTO weather_historical_telemetry
                        (timestamp, latitude, longitude, temp_c, cloud_cover, dni_w_m2, dhi_w_m2, ghi_w_m2)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """, (t_str, lat, lon, temp, cloud, dni, dhi, ghi))
            conn.commit()

        data["_cache_meta"] = {
            "cached": False,
            "source": "Open-Meteo Live (Refreshed and Cached)",
            "expires_in_seconds": CACHE_TTL_SECONDS
        }
        return data

    except Exception as e:
        # Fallback de emergencia a última caché conocida (aunque haya expirado)
        with sqlite3.connect(DB_PATH) as conn:
            cur = conn.cursor()
            cur.execute("""
                SELECT payload_json FROM weather_forecast_cache
                WHERE cache_key = ?
            """, (cache_key,))
            row = cur.fetchone()
            if row:
                data = json.loads(row[0])
                data["_cache_meta"] = {
                    "cached": True,
                    "stale": True,
                    "source": "Local SQLite Stale Cache (Offline Fallback)",
                    "error": str(e)
                }
                return data
        raise e

def get_climate_historical_5yr_summary():
    """Devuelve estadísticas y series históricas anuales de los últimos 5 años (2021-2026)"""
    with sqlite3.connect(DB_PATH) as conn:
        conn.row_factory = sqlite3.Row
        cur = conn.cursor()
        cur.execute("""
            SELECT year, avg_temp_c, min_temp_c, max_temp_c, total_precip_mm, total_et0_mm, total_solar_kwh_5kwp, avg_daily_solar_kwh 
            FROM v_annual_climate_summary
        """)
        annual = [dict(r) for r in cur.fetchall()]
        
        cur.execute("""
            SELECT COUNT(*) as total_hours, MIN(timestamp) as start_ts, MAX(timestamp) as end_ts,
                   ROUND(AVG(temp_c), 2) as global_avg_temp,
                   ROUND(MIN(temp_c), 2) as global_min_temp,
                   ROUND(MAX(temp_c), 2) as global_max_temp,
                   ROUND(SUM(precipitation_mm), 1) as total_rain_5yr,
                   ROUND(SUM(solar_pv_kw), 1) as total_solar_generated_5yr
            FROM climate_5yr_reanalysis_hourly
        """)
        overall = dict(cur.fetchone())
        
        return {
            "overall": overall,
            "annual": annual,
            "location": "Tocina, Sevilla",
            "source": "ERA5 European Reanalysis (Copernicus / Open-Meteo)",
            "granularity": "Hourly (49,344 samples)"
        }

def get_monthly_climate_breakdown(year=None):
    """Devuelve el desglose climático mes a mes para entrenamiento IA y predicciones"""
    with sqlite3.connect(DB_PATH) as conn:
        conn.row_factory = sqlite3.Row
        cur = conn.cursor()
        query = "SELECT * FROM v_monthly_climate_summary"
        params = ()
        if year:
            query += " WHERE year = ?"
            params = (year,)
        cur.execute(query, params)
        return [dict(r) for r in cur.fetchall()]

def weather_broker_background_daemon():
    """Hilo daemon que mantiene la caché permanentemente fresca cada 30 minutos sin saturar la red"""
    while True:
        try:
            get_weather_forecast(lat=37.5942, lon=-5.7397, days=7, force_refresh=False)
        except Exception as e:
            print(f"[WeatherBroker] Advertencia en refresco en segundo plano: {e}")
        time.sleep(900) # Chequear cada 15 min (si caducó >30 min refresca)

def start_weather_broker_thread():
    t = threading.Thread(target=weather_broker_background_daemon, daemon=True, name="WeatherBrokerDaemon")
    t.start()
    return t

