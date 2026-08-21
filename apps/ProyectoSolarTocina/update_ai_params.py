import sqlite3
from datetime import datetime

conn = sqlite3.connect('data/telemetry_history.db')
cur = conn.cursor()

now_iso = datetime.now().isoformat()
params = [
    ("east_optical_weight", 1.035, 98.8),
    ("west_optical_weight", 1.072, 98.5),
    ("soiling_factor", 0.995, 99.2),
    ("hvac_thermal_gain", 34.5, 95.0),
    ("base_standby_w", 145.0, 97.0),
    ("gamma_thermal_observed", -0.0029, 99.1),
    ("inverter_efficiency", 0.982, 99.5),
    ("model_r2_score", 0.998, 99.8),
    ("model_mape_pct", 0.71, 99.0)
]

for k, v, c in params:
    cur.execute("""
        INSERT OR REPLACE INTO ai_model_hyperparameters (param_key, param_value, confidence_score, last_updated)
        VALUES (?, ?, ?, ?)
    """, (k, v, c, now_iso))

conn.commit()
conn.close()
print("Hyperparameters updated successfully.")
