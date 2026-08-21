import sqlite3

conn = sqlite3.connect("telemetry_history.db")
c = conn.cursor()

c.execute("SELECT date_hour, sample_count FROM inverter_telemetry_hourly_rollup")
print("Rollups:", c.fetchall())
