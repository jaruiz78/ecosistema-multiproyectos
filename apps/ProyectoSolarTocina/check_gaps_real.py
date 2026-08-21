import sqlite3
from datetime import datetime

conn = sqlite3.connect("data/telemetry_history.db")
c = conn.cursor()

c.execute("SELECT timestamp FROM inverter_telemetry_history WHERE timestamp LIKE '2026-08-21%' ORDER BY timestamp ASC")
rows = c.fetchall()

if not rows:
    print("No data for today.")
else:
    timestamps = [datetime.fromisoformat(r[0]) for r in rows]
    print(f"Total readings today: {len(timestamps)}")
    print(f"First: {timestamps[0]}")
    print(f"Last:  {timestamps[-1]}")
    
    # Check for gaps > 5 minutes
    gaps = []
    for i in range(1, len(timestamps)):
        diff = (timestamps[i] - timestamps[i-1]).total_seconds()
        if diff > 300: # 5 minutes
            gaps.append((timestamps[i-1], timestamps[i], diff))
            
    if gaps:
        print(f"Gaps found: {len(gaps)}")
        for g in gaps:
            print(f" - From {g[0]} to {g[1]} (Gap: {g[2]/60:.1f} minutes)")
    else:
        print("No gaps larger than 5 minutes found.")
