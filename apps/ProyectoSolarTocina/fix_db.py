import re

with open("telemetry_db.py", "r") as f:
    code = f.read()

new_get_db = """from contextlib import contextmanager

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
"""

code = re.sub(
    r'def get_db\(\):.*?return conn',
    new_get_db,
    code,
    flags=re.DOTALL
)

with open("telemetry_db.py", "w") as f:
    f.write(code)
