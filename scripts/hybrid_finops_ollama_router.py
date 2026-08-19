#!/usr/bin/env python3
"""
Hybrid FinOps Router (Ollama Local / LiteRT vs Vertex AI / Gemini)
-----------------------------------------------------------------
Implementa la estrategia de conexión híbrida (FinOps < 0.015 USD/MAU):
1. Tareas mecánicas (parseo JSON, generación SBOM, linting): Delegadas a Ollama local ($0 coste).
2. Razonamiento arquitectónico y Consilium Romano: Enrutadas a Vertex AI / Gemini Pro.

@see docs/AGENTS.md
@see docs/adr/adr-004-firestore-rls-bigquery-finops.md
@reference Google Research (2024) Cost-Effective Multi-Model Orchestration
"""

import os
import sys
import json
import urllib.request
from typing import Dict, Any

OLLAMA_BASE_URL = os.getenv("OLLAMA_HOST", "http://localhost:11434")

def is_ollama_available() -> bool:
    try:
        req = urllib.request.Request(f"{OLLAMA_BASE_URL}/api/tags", method="GET")
        with urllib.request.urlopen(req, timeout=1.0) as response:
            return response.status == 200
    except Exception:
        return False

def route_task(task_type: str, payload: Dict[str, Any]) -> str:
    """
    Determina el motor de inferencia óptimo según el tipo de tarea y presupuesto FinOps.
    """
    mechanical_tasks = ["json_schema_validation", "cyclonedx_sbom", "format_code", "extract_ast"]
    
    if task_type in mechanical_tasks:
        if is_ollama_available():
            return "LOCAL_OLLAMA_ZERO_COST"
        else:
            return "LOCAL_LITERT_FALLBACK"
            
    return "CLOUD_GEMINI_VERTEX_AI"

def main():
    task = sys.argv[1] if len(sys.argv) > 1 else "json_schema_validation"
    target = route_task(task, {})
    ollama_ok = is_ollama_available()
    print(f"🎯 [FINOPS ROUTER] Tarea: '{task}' -> Target Óptimo: {target} (Ollama Local Activo: {ollama_ok})")

if __name__ == "__main__":
    main()
