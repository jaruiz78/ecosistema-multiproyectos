"""
Gestor de Base de Datos Local SQLite y Conector FoxCloud 2.0
Almacena telemetría en local y sincroniza huecos históricos desde la API oficial de Fox-ESS.
"""
import sqlite3
import os
import json
import time
from datetime import datetime
from contextlib import contextmanager

DB_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data")
DB_PATH = os.path.join(DB_DIR, "telemetry_history.db")
CONFIG_PATH = os.path.join(DB_DIR, "foxcloud_config.json")

os.makedirs(DB_DIR, exist_ok=True)

@contextmanager
def get_db():
    conn = sqlite3.connect(DB_PATH, timeout=15.0)
    conn.execute("PRAGMA journal_mode = WAL;")
    conn.execute("PRAGMA synchronous = NORMAL;")
    conn.execute("PRAGMA cache_size = -2000;")
    conn.execute("PRAGMA temp_store = MEMORY;")
    conn.execute("PRAGMA mmap_size = 30000000;")
    conn.row_factory = sqlite3.Row
    try:
        yield conn
    finally:
        try:
            conn.close()
        except Exception:
            pass

def init_db():
    with get_db() as conn:
        conn.execute("""
        CREATE TABLE IF NOT EXISTS inverter_telemetry_history (
            timestamp TEXT PRIMARY KEY,
            epoch_seconds INTEGER,
            pv1_voltage_v REAL,
            pv1_current_a REAL,
            pv1_power_w REAL,
            pv2_voltage_v REAL,
            pv2_current_a REAL,
            pv2_power_w REAL,
            solar_total_w REAL,
            solar_total_kw REAL,
            grid_voltage_v REAL,
            grid_current_a REAL,
            grid_ac_power_w REAL,
            grid_ac_power_kw REAL,
            grid_freq_hz REAL,
            battery_voltage_v REAL,
            battery_soc_percent REAL,
            battery_power_w REAL DEFAULT 0,
            home_load_w REAL DEFAULT 0,
            grid_import_w REAL DEFAULT 0,
            grid_export_w REAL DEFAULT 0,
            inverter_temp_c REAL,
            source TEXT DEFAULT 'modbus_local'
        );
        """)
        
        # Migración automática si la tabla ya existía
        cur = conn.cursor()
        cur.execute("PRAGMA table_info(inverter_telemetry_history)")
        cols = [c[1] for c in cur.fetchall()]
        for new_col in [("battery_power_w", "REAL DEFAULT 0"), 
                        ("home_load_w", "REAL DEFAULT 0"), 
                        ("grid_import_w", "REAL DEFAULT 0"), 
                        ("grid_export_w", "REAL DEFAULT 0")]:
            if new_col[0] not in cols:
                conn.execute(f"ALTER TABLE inverter_telemetry_history ADD COLUMN {new_col[0]} {new_col[1]}")

        conn.execute("""
        CREATE TABLE IF NOT EXISTS inverter_telemetry_hourly_rollup (
            date_hour TEXT PRIMARY KEY,
            avg_solar_w REAL,
            max_solar_w REAL,
            avg_grid_export_w REAL,
            avg_home_load_w REAL,
            avg_grid_import_w REAL,
            avg_battery_soc REAL,
            avg_inverter_temp REAL,
            sample_count INTEGER,
            created_at TIMESTAMP
        );
        """)
        conn.execute("CREATE INDEX IF NOT EXISTS idx_epoch ON inverter_telemetry_history(epoch_seconds);")
        conn.commit()

def compact_and_prune_history(retention_days=7):
    """
    Tiered Storage Compaction:
    - Agrega muestras de más de 7 días en resúmenes horarios (inverter_telemetry_hourly_rollup).
    - Purga las lecturas sub-minuto antiguas para mantener el archivo SQLite < 15MB de forma indefinida.
    """
    cutoff_epoch = int(time.time()) - (retention_days * 86400)
    with get_db() as conn:
        conn.execute("""
            INSERT OR REPLACE INTO inverter_telemetry_hourly_rollup
            (date_hour, avg_solar_w, max_solar_w, avg_grid_export_w, avg_home_load_w, avg_grid_import_w, avg_battery_soc, avg_inverter_temp, sample_count, created_at)
            SELECT 
                SUBSTR(timestamp, 1, 13) || ':00' as d_hour,
                ROUND(AVG(solar_total_w), 1),
                ROUND(MAX(solar_total_w), 1),
                ROUND(AVG(grid_export_w), 1),
                ROUND(AVG(home_load_w), 1),
                ROUND(AVG(grid_import_w), 1),
                ROUND(AVG(battery_soc_percent), 1),
                ROUND(AVG(inverter_temp_c), 1),
                COUNT(*),
                datetime('now')
            FROM inverter_telemetry_history
            WHERE epoch_seconds < ?
            GROUP BY SUBSTR(timestamp, 1, 13)
        """, (cutoff_epoch,))
        
        conn.execute("DELETE FROM inverter_telemetry_history WHERE epoch_seconds < ?", (cutoff_epoch,))
        conn.commit()

def save_telemetry_record(data, source='modbus_local'):
    """Guarda un registro de telemetría en SQLite de forma idempotente"""
    if not data or not data.get('online'):
        return False

    ts = data.get('timestamp') or datetime.now().isoformat()
    epoch = int(time.time())

    pv1 = data.get('pv1_east', {})
    pv2 = data.get('pv2_west', {})
    grid = data.get('grid', {})
    bat = data.get('battery', {})
    inv = data.get('inverter', {})

    pv1_w = pv1.get('power_w', 0)
    pv2_w = pv2.get('power_w', 0)
    solar_w = pv1_w + pv2_w

    home_load_w = grid.get('home_load_w', 0)
    grid_import_w = grid.get('grid_import_w', 0)
    grid_export_w = grid.get('grid_export_w', 0)
    bat_power_w = bat.get('power_w', 0)

    try:
        with get_db() as conn:
            conn.execute("""
            INSERT OR REPLACE INTO inverter_telemetry_history (
                timestamp, epoch_seconds,
                pv1_voltage_v, pv1_current_a, pv1_power_w,
                pv2_voltage_v, pv2_current_a, pv2_power_w,
                solar_total_w, solar_total_kw,
                grid_voltage_v, grid_current_a, grid_ac_power_w, grid_ac_power_kw, grid_freq_hz,
                battery_voltage_v, battery_soc_percent, battery_power_w,
                home_load_w, grid_import_w, grid_export_w,
                inverter_temp_c, source
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                ts, epoch,
                pv1.get('voltage_v', 0), pv1.get('current_a', 0), pv1_w,
                pv2.get('voltage_v', 0), pv2.get('current_a', 0), pv2_w,
                solar_w, round(solar_w / 1000.0, 3),
                grid.get('voltage_v', 0), grid.get('current_a', 0), grid.get('ac_power_w', 0), grid.get('ac_power_kw', 0), grid.get('freq_hz', 50.0),
                bat.get('voltage_v', 0), bat.get('soc_percent', 100), bat_power_w,
                home_load_w, grid_import_w, grid_export_w,
                inv.get('temperature_c', 0),
                source
            ))
            conn.commit()
        return True
    except Exception as e:
        print(f"Error guardando telemetría en SQLite: {e}")
        return False

def get_recent_history(limit=500):
    """Devuelve los últimos N registros históricos de telemetría"""
    with get_db() as conn:
        cursor = conn.execute("""
        SELECT * FROM inverter_telemetry_history 
        ORDER BY epoch_seconds DESC 
        LIMIT ?
        """, (limit,))
        rows = [dict(r) for r in cursor.fetchall()]
        return rows

def get_today_hourly_telemetry(date_str=None):
    """Devuelve la producción y consumos reales medidos agrupados por hora para el día en curso (0..23)"""
    now = datetime.now()
    if not date_str:
        date_str = now.strftime('%Y-%m-%d')
    current_hour = now.hour
    current_minute = now.minute

    with get_db() as conn:
        cursor = conn.execute("""
            SELECT 
                CAST(SUBSTR(timestamp, 12, 2) AS INTEGER) as hour,
                ROUND(AVG(solar_total_kw), 3) as avg_solar_kw,
                ROUND(MAX(solar_total_kw), 3) as max_solar_kw,
                ROUND(AVG(pv1_power_w) / 1000.0, 3) as avg_pv1_kw,
                ROUND(AVG(pv2_power_w) / 1000.0, 3) as avg_pv2_kw,
                ROUND(AVG(grid_ac_power_kw), 3) as avg_grid_kw,
                ROUND(AVG(CASE WHEN home_load_w > 0 THEN home_load_w / 1000.0 ELSE grid_ac_power_kw END), 3) as avg_home_kw,
                ROUND(AVG(grid_import_w) / 1000.0, 3) as avg_grid_import_kw,
                ROUND(AVG(grid_export_w) / 1000.0, 3) as avg_grid_export_kw,
                ROUND(AVG(battery_soc_percent), 1) as avg_battery_soc,
                ROUND(AVG(battery_power_w), 1) as avg_battery_power_w,
                ROUND(AVG(inverter_temp_c), 1) as avg_inverter_temp,
                COUNT(*) as sample_count
            FROM inverter_telemetry_history
            WHERE timestamp LIKE ?
            GROUP BY CAST(SUBSTR(timestamp, 12, 2) AS INTEGER)
            ORDER BY hour ASC
        """, (f"{date_str}%",))
        rows = [dict(r) for r in cursor.fetchall()]
        return {
            "date": date_str,
            "current_hour": current_hour,
            "current_minute": current_minute,
            "hourly": rows
        }

def get_history_stats():
    """Devuelve estadísticas de la base de datos local"""
    with get_db() as conn:
        cursor = conn.execute("""
        SELECT 
            COUNT(*) as total_records,
            MIN(timestamp) as first_record,
            MAX(timestamp) as last_record,
            MAX(solar_total_w) as max_solar_w
        FROM inverter_telemetry_history
        """)
        row = dict(cursor.fetchone())
        return row

# Inicializar BD al importar
init_db()
