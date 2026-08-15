#!/usr/bin/env python3
import time
import sqlite3
import numpy as np
from pathlib import Path

def get_simulation_tables(conn):
    cursor = conn.cursor()
    cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'proyecto%_simulations'")
    return [row[0] for row in cursor.fetchall()]

def main():
    print("🌍 [Gemelo Digital O(1)] Arrancando simulador vectorizado a 5 años (1M eventos/vertical)...")
    
    base_dir = Path("/home/jaruiz/Desarrollo")
    db_path = base_dir / "simulations_telemetry.db"
    
    SIMULATIONS_PER_VERTICAL = 1_000_000  # 1 millón para 5 años
    
    with sqlite3.connect(db_path) as conn:
        # Optimizar SQLite para inserciones masivas
        conn.execute("PRAGMA synchronous = OFF")
        conn.execute("PRAGMA journal_mode = MEMORY")
        conn.execute("PRAGMA temp_store = MEMORY")
        
        tables = get_simulation_tables(conn)
        print(f"📊 Verticals identificados: {len(tables)}. Total de eventos a proyectar: {len(tables) * SIMULATIONS_PER_VERTICAL:,}")
        
        start_time = time.time()
        
        for table in tables:
            print(f"   -> Simulando 1M eventos para: {table} ...", end="", flush=True)
            
            # Generar datos de manera vectorizada
            latencies = np.random.normal(loc=15.0, scale=3.0, size=SIMULATIONS_PER_VERTICAL)
            latencies = np.clip(latencies, 1.0, 100.0)
            
            # Probabilidad de éxito ~99.9%
            successes = np.random.choice([1, 0], size=SIMULATIONS_PER_VERTICAL, p=[0.999, 0.001])
            
            # Preparar la lista de tuplas nativas O(N) memory
            # tuple map en numpy es algo lento, una compresión a lista funciona
            records = list(zip(latencies.tolist(), successes.tolist()))
            
            # Limpiar la tabla si queremos simular los 5 años puros (o añadir, pero para limpiar espacio)
            # Vamos a vaciar y meter los 1M
            conn.execute(f"DELETE FROM {table}")
            
            conn.executemany(
                f"INSERT INTO {table} (p50_latency_ms, success) VALUES (?, ?)",
                records
            )
            print(" [OK]")
            
        conn.commit()
        
    duration = time.time() - start_time
    print(f"✅ Gemelo Digital finalizado. Simulados {len(tables) * SIMULATIONS_PER_VERTICAL:,} eventos en {duration:.2f} segundos.")

if __name__ == "__main__":
    main()
