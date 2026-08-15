#!/usr/bin/env python3
import os
import json
import sqlite3
from pathlib import Path
from datetime import datetime

def main():
    print("🚀 [Ingesta O(1)] Iniciando escaneo de archivos .jsonl en el ecosistema...")
    base_dir = Path("/home/jaruiz/Desarrollo")
    
    jsonl_files = list(base_dir.rglob("*.jsonl"))
    db_path = base_dir / "simulations_telemetry.db"
    
    with sqlite3.connect(db_path) as conn:
        conn.execute("""
            CREATE TABLE IF NOT EXISTS ai_training_dataset (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                source_file TEXT,
                instruction TEXT,
                input TEXT,
                output TEXT,
                metadata_json TEXT,
                ingested_at TIMESTAMP
            )
        """)
        
        total_ingested = 0
        batch_size = 5000
        batch = []
        
        for file_path in jsonl_files:
            if ".gemini" in file_path.parts or "node_modules" in file_path.parts:
                continue
                
            print(f"📥 Leyendo {file_path.name}...")
            with open(file_path, "r", encoding="utf-8") as f:
                for line in f:
                    if not line.strip(): continue
                    try:
                        record = json.loads(line)
                        inst = record.get("instruction") or record.get("prompt") or str(record.get("text", "")) or "N/A"
                        inp = record.get("input") or record.get("context") or "N/A"
                        out = record.get("output") or record.get("chosen") or record.get("response") or "N/A"
                        meta = json.dumps(record.get("metadata", {}))
                        
                        batch.append((str(file_path.name), inst, inp, out, meta, datetime.utcnow()))
                        
                        if len(batch) >= batch_size:
                            conn.executemany(
                                "INSERT INTO ai_training_dataset (source_file, instruction, input, output, metadata_json, ingested_at) VALUES (?, ?, ?, ?, ?, ?)",
                                batch
                            )
                            total_ingested += len(batch)
                            batch.clear()
                    except json.JSONDecodeError:
                        pass
                        
        if batch:
            conn.executemany(
                "INSERT INTO ai_training_dataset (source_file, instruction, input, output, metadata_json, ingested_at) VALUES (?, ?, ?, ?, ?, ?)",
                batch
            )
            total_ingested += len(batch)
            
        conn.commit()
    
    print(f"✅ Ingesta Completa: {total_ingested} registros consolidados en el Knowledge Graph unificado.")

if __name__ == "__main__":
    main()
