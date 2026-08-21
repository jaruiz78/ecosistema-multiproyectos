import sqlite3
import os

new_db = "telemetry_history.db" # The one in root
old_db = "data/telemetry_history.db"

if os.path.exists(new_db) and os.path.exists(old_db):
    conn_new = sqlite3.connect(new_db)
    conn_new.row_factory = sqlite3.Row
    c_new = conn_new.cursor()
    c_new.execute("SELECT * FROM inverter_telemetry_history")
    rows = c_new.fetchall()
    
    conn_old = sqlite3.connect(old_db)
    c_old = conn_old.cursor()
    
    count = 0
    for r in rows:
        d = dict(r)
        # Assuming table structures are identical
        placeholders = ", ".join(["?"] * len(d))
        columns = ", ".join(d.keys())
        sql = f"INSERT OR IGNORE INTO inverter_telemetry_history ({columns}) VALUES ({placeholders})"
        c_old.execute(sql, tuple(d.values()))
        count += 1
        
    conn_old.commit()
    print(f"Migrados {count} registros del DB temporal al principal.")
    
    # Do the same for rollups if any
    c_new.execute("SELECT * FROM inverter_telemetry_hourly_rollup")
    rollups = c_new.fetchall()
    for r in rollups:
        d = dict(r)
        placeholders = ", ".join(["?"] * len(d))
        columns = ", ".join(d.keys())
        sql = f"INSERT OR IGNORE INTO inverter_telemetry_hourly_rollup ({columns}) VALUES ({placeholders})"
        c_old.execute(sql, tuple(d.values()))
        
    conn_old.commit()
    conn_new.close()
    conn_old.close()
    
    # Backup and delete the temporary db
    os.rename(new_db, new_db + ".migrated")
else:
    print("Files not found.")
