#!/usr/bin/env python3
"""
Módulo Corporativo de Mantenimiento, Poda y Compactación de Almacenamiento Local
Aplica:
 1. Auto-vacuum y eliminación de registros obsoletos (>15 días) en SQLite (simulations_telemetry.db, ldjs.db).
 2. Rotación y compresión gzip de logs sobredimensionados (>5MB) en carpetas logs/.
 3. Reporte de espacio recuperado en megabytes.
"""

import os
import gzip
import shutil
import sqlite3
import glob
from datetime import datetime, timezone

WORKSPACE_ROOT = "/home/jaruiz/Desarrollo"

SQLITE_DBS = [
    os.path.join(WORKSPACE_ROOT, "simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "data/simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "SaaSRegantes/simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "AppViajes/simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "PCT/PCT_TASKS/pctMultiMicroservices/simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "corp-spring-boot-starter/simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "corp-spring-boot-starter/logs/simulations_telemetry.db"),
    os.path.join(WORKSPACE_ROOT, "apps/JobsSearch/ldjs.db")
]

LOG_DIRS = [
    os.path.join(WORKSPACE_ROOT, "logs"),
    os.path.join(WORKSPACE_ROOT, "SaaSRegantes/logs"),
    os.path.join(WORKSPACE_ROOT, "AppViajes/logs"),
    os.path.join(WORKSPACE_ROOT, "PCT/PCT_TASKS/pctMultiMicroservices/logs"),
    os.path.join(WORKSPACE_ROOT, "corp-spring-boot-starter/logs"),
    os.path.join(WORKSPACE_ROOT, "apps/JobsSearch")
]

def get_file_size_mb(filepath):
    if os.path.exists(filepath):
        return os.path.getsize(filepath) / (1024 * 1024)
    return 0.0

def prune_and_vacuum_sqlite(db_path):
    if not os.path.exists(db_path):
        return 0.0, 0.0, 0
    
    initial_size = get_file_size_mb(db_path)
    deleted_rows = 0
    
    try:
        conn = sqlite3.connect(db_path)
        cursor = conn.cursor()
        
        # Obtener todas las tablas
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
        tables = [row[0] for row in cursor.fetchall() if not row[0].startswith("sqlite_")]
        
        for table in tables:
            # Comprobar si tiene columna timestamp o date
            cursor.execute(f"PRAGMA table_info({table});")
            columns = [col[1].lower() for col in cursor.fetchall()]
            
            time_col = None
            for candidate in ["timestamp", "created_at", "fecha", "date"]:
                if candidate in columns:
                    time_col = candidate
                    break
            
            if time_col:
                try:
                    cursor.execute(f"DELETE FROM {table} WHERE {time_col} < datetime('now', '-15 days');")
                    deleted_rows += cursor.rowcount
                except Exception:
                    pass
        
        conn.commit()
        cursor.execute("VACUUM;")
        conn.commit()
        conn.close()
    except Exception as e:
        print(f"  [WARN] Error procesando {db_path}: {e}")
        return initial_size, initial_size, 0
        
    final_size = get_file_size_mb(db_path)
    return initial_size, final_size, deleted_rows

def compress_oversized_logs():
    compressed_files = []
    total_saved_mb = 0.0
    
    for log_dir in LOG_DIRS:
        if not os.path.exists(log_dir):
            continue
        
        for log_file in glob.glob(os.path.join(log_dir, "*.log")):
            size_mb = get_file_size_mb(log_file)
            # Comprimir o truncar archivos mayores a 2MB
            if size_mb > 2.0:
                gz_path = f"{log_file}.gz"
                initial_size = size_mb
                
                # Comprimir a .gz
                with open(log_file, 'rb') as f_in:
                    with gzip.open(gz_path, 'wb') as f_out:
                        shutil.copyfileobj(f_in, f_out)
                
                # Truncar o eliminar el archivo original dejando cabecera
                with open(log_file, 'w', encoding='utf-8') as f_in:
                    f_in.write(f"--- LOG ROTATED AND COMPRESSED ON {datetime.now(timezone.utc).isoformat()} TO {os.path.basename(gz_path)} ---\n")
                
                final_size = get_file_size_mb(log_file) + get_file_size_mb(gz_path)
                saved = initial_size - final_size
                total_saved_mb += saved
                compressed_files.append((log_file, initial_size, final_size, saved))
                
    return compressed_files, total_saved_mb

def run_full_storage_maintenance():
    print("==========================================================================")
    print("      MANTENIMIENTO, PODA Y COMPACTACIÓN DE ALMACENAMIENTOS LOCALES       ")
    print("==========================================================================")
    
    total_initial_db_mb = 0.0
    total_final_db_mb = 0.0
    total_rows_deleted = 0
    
    print("\n1. Compactación y Poda de Bases de Datos SQLite:")
    for db in SQLITE_DBS:
        if os.path.exists(db):
            init_s, fin_s, rows = prune_and_vacuum_sqlite(db)
            saved = init_s - fin_s
            total_initial_db_mb += init_s
            total_final_db_mb += fin_s
            total_rows_deleted += rows
            print(f"  • {os.path.relpath(db, WORKSPACE_ROOT):<60} | {init_s:.2f} MB -> {fin_s:.2f} MB (Ahorro: {saved:.2f} MB, Filas podadas: {rows})")
            
    print(f"\n  -> Total SQLite: {total_initial_db_mb:.2f} MB -> {total_final_db_mb:.2f} MB | Ahorrado: {total_initial_db_mb - total_final_db_mb:.2f} MB")
    
    print("\n2. Rotación y Compresión de Archivos de Log Sobredimensionados:")
    compressed, saved_log_mb = compress_oversized_logs()
    for log_path, init_s, fin_s, saved in compressed:
        print(f"  • {os.path.relpath(log_path, WORKSPACE_ROOT):<60} | {init_s:.2f} MB -> {fin_s:.2f} MB (Ahorro: {saved:.2f} MB)")
        
    print(f"\n  -> Total Logs Ahorrado: {saved_log_mb:.2f} MB")
    
    total_reclaimed = (total_initial_db_mb - total_final_db_mb) + saved_log_mb
    print("\n==========================================================================")
    print(f"  RESUMEN TOTAL ESPACIO RECUPERADO: {total_reclaimed:.2f} MB")
    print("==========================================================================")
    return total_reclaimed

if __name__ == "__main__":
    run_full_storage_maintenance()
