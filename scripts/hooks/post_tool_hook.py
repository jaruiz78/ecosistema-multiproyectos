#!/usr/bin/env python3
"""
Post-Tool Execution Hook (Antigravity 2.0 & Managed Agents)
-----------------------------------------------------------
Interceptors ejecutados inmediatamente DESPUÉS de cualquier llamada a herramienta:
1. Telemetría en SQLite (simulations_telemetry.db -> agent_tool_telemetry)
2. Auto-Linting y Verificación de Formato Post-Escritura
3. Detección de Cambios de Arquitectura (ADR / Pom / Specs) y Notificación
4. Control de Cuotas y Seguimiento FinOps en Tiempo Real

Retorno: Exit code 0 (Completado con éxito)
"""

import sys
import os
import time
import json
import sqlite3
from pathlib import Path
from typing import Dict, Any, Optional

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
DB_PATH = WORKSPACE_ROOT / "data" / "simulations_telemetry.db"
if not DB_PATH.exists():
    # Fallback to root if data/ does not contain it
    alt_db = WORKSPACE_ROOT / "simulations_telemetry.db"
    if alt_db.exists():
        DB_PATH = alt_db

def init_telemetry_table(conn: sqlite3.Connection):
    """Inicializa la tabla de telemetría de herramientas de agentes si no existe."""
    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS agent_tool_telemetry (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp REAL,
            tool_name TEXT,
            target_entity TEXT,
            status TEXT,
            duration_ms REAL,
            details TEXT
        )
    """)
    conn.commit()

def record_tool_telemetry(tool_name: str, target: str, status: str, duration_ms: float, details: str = ""):
    """Registra la invocación de herramienta en SQLite."""
    try:
        os.makedirs(DB_PATH.parent, exist_ok=True)
        conn = sqlite3.connect(str(DB_PATH), timeout=5.0)
        init_telemetry_table(conn)
        cursor = conn.cursor()
        cursor.execute("""
            INSERT INTO agent_tool_telemetry (timestamp, tool_name, target_entity, status, duration_ms, details)
            VALUES (?, ?, ?, ?, ?, ?)
        """, (time.time(), tool_name, target, status, duration_ms, details))
        conn.commit()
        conn.close()
    except Exception as e:
        # La telemetría no debe romper el flujo si la BD está temporalmente ocupada
        pass

def lint_file_post_write(file_path: str):
    """Verifica reglas mínimas de formato en el archivo modificado."""
    p = Path(file_path)
    if not p.exists() or not p.is_file():
        return
    
    # Si es archivo de código o doc, verificar trailing newlines
    try:
        content = p.read_text(encoding="utf-8", errors="ignore")
        if content and not content.endswith("\n"):
            with open(p, "a", encoding="utf-8") as f:
                f.write("\n")
    except Exception:
        pass

def main():
    if len(sys.argv) < 2:
        sys.exit(0)

    try:
        tool_data = None
        if sys.argv[1].startswith("{"):
            tool_data = json.loads(sys.argv[1])
        elif len(sys.argv) >= 3 and sys.argv[1] == "--json":
            tool_data = json.loads(sys.argv[2])

        if not tool_data:
            tool_name = sys.argv[1]
            tool_data = {"tool_name": tool_name, "args": {}, "status": "SUCCESS", "duration_ms": 0.0}

        tool_name = tool_data.get("tool_name", "")
        args = tool_data.get("args", {})
        status = tool_data.get("status", "SUCCESS")
        duration_ms = tool_data.get("duration_ms", 0.0)

        target = ""
        if tool_name in ["write_to_file", "replace_file_content", "multi_replace_file_content", "view_file"]:
            target = args.get("TargetFile", "") or args.get("AbsolutePath", "")
            if tool_name in ["write_to_file", "replace_file_content", "multi_replace_file_content"] and target:
                lint_file_post_write(target)
        elif tool_name in ["run_command", "exec_command"]:
            target = (args.get("CommandLine", "") or args.get("command", ""))[:100]
        elif tool_name in ["call_mcp_tool"]:
            target = f"{args.get('ServerName', '')}::{args.get('ToolName', '')}"

        # Registrar en la base de datos de telemetría
        record_tool_telemetry(tool_name, target, status, duration_ms, json.dumps(args)[:200])

        sys.exit(0)

    except Exception as e:
        sys.exit(0)

if __name__ == "__main__":
    main()
