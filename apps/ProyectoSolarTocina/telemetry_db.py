import sqlite3
import os
import time
from datetime import datetime
import threading

DB_DIR = os.path.dirname(os.path.abspath(__file__))
CONFIG_PATH = os.path.join(DB_DIR, "data", "foxcloud_config.json")
DB_FILE = os.path.join(DB_DIR, "data", "telemetry_history.db")

# Micro-batching state
_batch_lock = threading.Lock()
_telemetry_batch = []
BATCH_SIZE_LIMIT = 20  # Save every 20 records (roughly 60 seconds if polled every 3s)
LAST_FLUSH_TIME = time.time()
FLUSH_INTERVAL_SEC = 60

from contextlib import contextmanager

@contextmanager
def get_db():
    conn = sqlite3.connect(DB_FILE, check_same_thread=False)
    conn.row_factory = sqlite3.Row
    # Optimizations for TSDB insertion
    conn.execute("PRAGMA journal_mode=WAL;")
    conn.execute("PRAGMA synchronous=NORMAL;")
    conn.execute("PRAGMA temp_store=MEMORY;")
    try:
        with conn:
            yield conn
    finally:
        conn.close()


def init_db():
    with get_db() as conn:
        conn.execute("""
        CREATE TABLE IF NOT EXISTS inverter_telemetry_history (
            timestamp TEXT PRIMARY KEY,
            epoch_seconds INTEGER,
            pv1_voltage_v REAL, pv1_current_a REAL, pv1_power_w REAL,
            pv2_voltage_v REAL, pv2_current_a REAL, pv2_power_w REAL,
            solar_total_w REAL, solar_total_kw REAL,
            grid_voltage_v REAL, grid_current_a REAL, grid_ac_power_w REAL, grid_ac_power_kw REAL, grid_freq_hz REAL,
            battery_voltage_v REAL, battery_soc_percent REAL, battery_power_w REAL,
            home_load_w REAL, grid_import_w REAL, grid_export_w REAL,
            inverter_temp_c REAL, source TEXT
        );
        """)
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

def _flush_telemetry_batch():
    global _telemetry_batch, LAST_FLUSH_TIME
    with _batch_lock:
        if not _telemetry_batch:
            return
        
        batch_copy = _telemetry_batch[:]
        _telemetry_batch = []
        LAST_FLUSH_TIME = time.time()
        
    if not batch_copy:
        return
        
    try:
        with get_db() as conn:
            conn.executemany("""
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
            """, batch_copy)
            conn.commit()
    except Exception as e:
        print(f"Error guardando lote de telemetría en SQLite: {e}")

def save_telemetry_record(data, source='modbus_local'):
    """Guarda un registro de telemetría en SQLite usando Micro-batching O(1) disk writes"""
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

    record_tuple = (
        ts, epoch,
        pv1.get('voltage_v', 0), pv1.get('current_a', 0), pv1_w,
        pv2.get('voltage_v', 0), pv2.get('current_a', 0), pv2_w,
        solar_w, round(solar_w / 1000.0, 3),
        grid.get('voltage_v', 0), grid.get('current_a', 0), grid.get('ac_power_w', 0), grid.get('ac_power_kw', 0), grid.get('freq_hz', 50.0),
        bat.get('voltage_v', 0), bat.get('soc_percent', 100), bat_power_w,
        home_load_w, grid_import_w, grid_export_w,
        inv.get('temperature_c', 0),
        source
    )
    
    global LAST_FLUSH_TIME
    with _batch_lock:
        _telemetry_batch.append(record_tuple)
        batch_len = len(_telemetry_batch)
        time_since_flush = time.time() - LAST_FLUSH_TIME
    
    if batch_len >= BATCH_SIZE_LIMIT or time_since_flush >= FLUSH_INTERVAL_SEC:
        _flush_telemetry_batch()
        
    return True

def get_recent_history(limit=500):
    _flush_telemetry_batch()  # Ensure pending are written before query
    with get_db() as conn:
        cursor = conn.execute("""
        SELECT * FROM inverter_telemetry_history 
        ORDER BY epoch_seconds DESC 
        LIMIT ?
        """, (limit,))
        rows = [dict(r) for r in cursor.fetchall()]
        return rows

def get_today_hourly_telemetry(date_str=None):
    _flush_telemetry_batch()
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

def get_today_high_res_telemetry(date_str=None):
    _flush_telemetry_batch()
    now = datetime.now()
    if not date_str:
        date_str = now.strftime('%Y-%m-%d')
    current_hour = now.hour
    current_minute = now.minute

    with get_db() as conn:
        cursor = conn.execute("""
            SELECT 
                SUBSTR(timestamp, 12, 5) as time_min,
                CAST(SUBSTR(timestamp, 12, 2) AS INTEGER) as hour,
                CAST(SUBSTR(timestamp, 15, 2) AS INTEGER) as minute,
                ROUND(AVG(solar_total_kw), 3) as avg_solar_kw,
                ROUND(AVG(CASE WHEN home_load_w > 0 THEN home_load_w / 1000.0 ELSE grid_ac_power_kw END), 3) as avg_home_kw,
                ROUND(AVG(grid_import_w) / 1000.0, 3) as avg_grid_import_kw,
                ROUND(AVG(grid_export_w) / 1000.0, 3) as avg_grid_export_kw,
                ROUND(AVG(battery_soc_percent), 1) as avg_battery_soc,
                ROUND(AVG(battery_power_w), 1) as avg_battery_power_w,
                COUNT(*) as sample_count
            FROM inverter_telemetry_history
            WHERE timestamp LIKE ?
            GROUP BY SUBSTR(timestamp, 12, 5)
            ORDER BY time_min ASC
        """, (f"{date_str}%",))
        rows = [dict(r) for r in cursor.fetchall()]
        return {
            "date": date_str,
            "current_hour": current_hour,
            "current_minute": current_minute,
            "timeline": rows
        }

def get_history_stats():
    _flush_telemetry_batch()
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

init_db()
