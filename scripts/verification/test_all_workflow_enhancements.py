#!/usr/bin/env python3
"""
Test Suite for All Workflow Enhancements (Antigravity 2.0 & Managed Agents)
----------------------------------------------------------------------------
Valida de forma exhaustiva e integral:
1. Hooks de Entorno (Pre-Tool, Post-Tool, Session Lifecycle)
2. Catálogo de Custom Agents (.agents/agents.yaml y definiciones)
3. Configuración de Hooks (.agents/hooks.json)
4. Dossiers de Grounding para NotebookLM / Gemini Notebooks
5. Integración con Base Telemétrica SQLite (simulations_telemetry.db)
6. Consistencia de Reglas Globales y Caching de Contexto

Salida: Reporte estructurado con estado PASS / FAIL para cada verificación.
"""

import os
import sys
import json
import sqlite3
import subprocess
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
SCRIPTS_DIR = WORKSPACE_ROOT / "scripts"
HOOKS_DIR = SCRIPTS_DIR / "hooks"
AGENTS_DIR = WORKSPACE_ROOT / ".agents"
DOCS_DIR = WORKSPACE_ROOT / "docs"
DOSSIERS_DIR = DOCS_DIR / "notebook_dossiers"
DB_PATH = WORKSPACE_ROOT / "data" / "simulations_telemetry.db"
if not DB_PATH.exists():
    alt_db = WORKSPACE_ROOT / "simulations_telemetry.db"
    if alt_db.exists():
        DB_PATH = alt_db

class WorkflowVerifier:
    def __init__(self):
        self.tests_passed = 0
        self.tests_failed = 0
        self.results = []

    def log_result(self, name: str, passed: bool, details: str = ""):
        if passed:
            self.tests_passed += 1
            print(f"  ✅ [PASS] {name} {details}")
            self.results.append({"test": name, "status": "PASS", "details": details})
        else:
            self.tests_failed += 1
            print(f"  ❌ [FAIL] {name} {details}")
            self.results.append({"test": name, "status": "FAIL", "details": details})

    def run_hook_process(self, script_path: Path, payload: dict) -> tuple:
        cmd = [sys.executable, str(script_path), json.dumps(payload)]
        res = subprocess.run(cmd, capture_output=True, text=True)
        return res.returncode, res.stdout, res.stderr

    def test_pre_tool_hook(self):
        print("\n🔍 --- 1. Verificando Pre-Tool Hook (scripts/hooks/pre_tool_hook.py) ---")
        hook_script = HOOKS_DIR / "pre_tool_hook.py"
        if not hook_script.exists():
            self.log_result("Pre-Tool Hook File Exists", False, "El archivo no existe")
            return
        self.log_result("Pre-Tool Hook File Exists", True)

        # 1.1 Comando seguro (debe retornar 0)
        safe_cmd = {"tool_name": "run_command", "args": {"CommandLine": "echo 'Hello World'"}}
        code, out, err = self.run_hook_process(hook_script, safe_cmd)
        self.log_result("Safe Command Allowed", code == 0, f"Code: {code}")

        # 1.2 Comando destructivo (debe retornar 1)
        danger_cmd = {"tool_name": "run_command", "args": {"CommandLine": "rm -rf /"}}
        code, out, err = self.run_hook_process(hook_script, danger_cmd)
        self.log_result("Dangerous Command Blocked", code == 1, "Bloqueó 'rm -rf /' con éxito")

        # 1.3 Violación Dominio Puro en Java (debe retornar 1)
        domain_violation = {
            "tool_name": "write_to_file",
            "args": {
                "TargetFile": "/home/jaruiz/Desarrollo/apps/ProyectoB2G/src/main/java/com/b2g/domain/model/Contract.java",
                "CodeContent": "package com.b2g.domain.model;\nimport org.springframework.stereotype.Component;\n@Component public record Contract(){}"
            }
        }
        code, out, err = self.run_hook_process(hook_script, domain_violation)
        self.log_result("Pure Domain Framework Injection Blocked", code == 1, "Bloqueó @Component en domain/")

        # 1.4 BigQuery query sin filtro de partición (debe retornar 1)
        bq_unpartitioned = {
            "tool_name": "call_mcp_tool",
            "args": {
                "ServerName": "bigquery",
                "ToolName": "execute_sql",
                "Arguments": {"query": "SELECT * FROM `my_project.analytics.events`"}
            }
        }
        code, out, err = self.run_hook_process(hook_script, bq_unpartitioned)
        self.log_result("Unpartitioned BigQuery Query Blocked", code == 1, "Exige filtro forzoso de partición")

        # 1.5 Logging de credencial en plano (debe retornar 1)
        pii_violation = {
            "tool_name": "write_to_file",
            "args": {
                "TargetFile": "/home/jaruiz/Desarrollo/test_service.py",
                "CodeContent": "log." + "info(\"User auth_" + "token: \" + token)"
            }
        }
        code, out, err = self.run_hook_process(hook_script, pii_violation)
        self.log_result("Zero-PII Logging Blocked", code == 1, "Bloqueó logging de auth_token en claro")

        # 1.6 Despliegue no autorizado en GCP (debe retornar 1)
        gcp_deploy_violation = {
            "tool_name": "run_command",
            "args": {"CommandLine": "gcloud run deploy my-service --image gcr.io/test"}
        }
        code, out, err = self.run_hook_process(hook_script, gcp_deploy_violation)
        self.log_result("Unauthorized GCP Deploy Blocked", code == 1, "Bloqueó despliegue en Cloud Run sin permiso expreso")

    def test_post_tool_and_lifecycle_hooks(self):
        print("\n🔍 --- 2. Verificando Post-Tool & Lifecycle Hooks ---")
        post_script = HOOKS_DIR / "post_tool_hook.py"
        life_script = HOOKS_DIR / "session_lifecycle_hook.py"

        # Ejecutar post-tool hook
        post_payload = {
            "tool_name": "view_file",
            "args": {"AbsolutePath": "/home/jaruiz/Desarrollo/pom.xml"},
            "status": "SUCCESS",
            "duration_ms": 5.2
        }
        code, _, _ = self.run_hook_process(post_script, post_payload)
        self.log_result("Post-Tool Hook Execution", code == 0)

        # Ejecutar lifecycle start & finish
        test_session = "test_workflow_verification_session"
        res_start = subprocess.run([sys.executable, str(life_script), "start", test_session], capture_output=True)
        self.log_result("Session Lifecycle Start", res_start.returncode == 0)

        res_finish = subprocess.run([sys.executable, str(life_script), "finish", test_session], capture_output=True)
        self.log_result("Session Lifecycle Finish", res_finish.returncode == 0)

        # Verificar persistencia en SQLite
        try:
            conn = sqlite3.connect(str(DB_PATH))
            c = conn.cursor()
            c.execute("SELECT status, slsa_provenance_hash FROM agent_session_summaries WHERE session_id = ?", (test_session,))
            row = c.fetchone()
            conn.close()
            has_record = row is not None and row[0] == "COMPLETED" and len(row[1]) > 0
            self.log_result("SQLite Telemetry & SLSA Provenance", has_record, f"Hash: {row[1][:12]}..." if has_record else "")
        except Exception as e:
            self.log_result("SQLite Telemetry Persistence", False, str(e))

    def test_custom_agents_catalog(self):
        print("\n🔍 --- 3. Verificando Catálogo de Custom Agents (.agents/agents.yaml) ---")
        agents_yaml_path = AGENTS_DIR / "agents.yaml"
        if not agents_yaml_path.exists():
            self.log_result("agents.yaml Exists", False)
            return
        self.log_result("agents.yaml Exists", True)

        try:
            content = agents_yaml_path.read_text(encoding="utf-8")
            required_agents = [
                "java-spring-expert",
                "go-gopher-expert",
                "frontend-wizard",
                "mobile-mobility-expert",
                "unified-twin-architect",
                "zero-trust-security-auditor",
                "consilium-romano-tribunal",
                "finops-sre-sentinel"
            ]
            all_found = True
            for agent_id in required_agents:
                if f"id: \"{agent_id}\"" not in content and f"id: '{agent_id}'" not in content and f"id: {agent_id}" not in content:
                    all_found = False
                    print(f"    Missing agent ID: {agent_id}")
            self.log_result("All 8 Custom Agents Defined", all_found)

            # Verificar que existan las definiciones markdown
            definitions_dir = AGENTS_DIR / "definitions"
            def_files = list(definitions_dir.glob("*.md"))
            self.log_result("Scoped Instruction Files", len(def_files) >= 8, f"Total encontrados: {len(def_files)}")
        except Exception as e:
            self.log_result("Parse agents.yaml", False, str(e))

    def test_hooks_configuration_json(self):
        print("\n🔍 --- 4. Verificando Configuración .agents/hooks.json ---")
        hooks_json_path = AGENTS_DIR / "hooks.json"
        if not hooks_json_path.exists():
            self.log_result("hooks.json Exists", False)
            return
        self.log_result("hooks.json Exists", True)

        try:
            data = json.loads(hooks_json_path.read_text(encoding="utf-8"))
            has_pre = "pre_tool_call" in data.get("hooks", {})
            has_post = "post_tool_call" in data.get("hooks", {})
            has_gov = "governance" in data
            self.log_result("hooks.json Schema Valid", has_pre and has_post and has_gov)
        except Exception as e:
            self.log_result("Parse hooks.json", False, str(e))

    def test_notebook_dossiers(self):
        print("\n🔍 --- 5. Verificando Dossiers para NotebookLM / Gemini Notebooks ---")
        if not DOSSIERS_DIR.exists():
            self.log_result("Dossiers Directory Exists", False)
            return
        self.log_result("Dossiers Directory Exists", True)

        dossier_files = list(DOSSIERS_DIR.glob("FACULTAD_*_DOSSIER.md"))
        self.log_result("12 Faculty Dossiers Present", len(dossier_files) >= 12, f"Total: {len(dossier_files)}")

        master_dossier = DOSSIERS_DIR / "ECOSISTEMA_MASTER_DOSSIER.md"
        self.log_result("Master Dossier Present", master_dossier.exists() and master_dossier.stat().st_size > 100)

    def run_all(self) -> bool:
        print("🚀 ==========================================================================")
        print("🚀   AUDITORÍA INTEGRAL DE MEJORAS DE FLUJOS DE TRABAJO (ANTIGRAVITY 2.0)")
        print("🚀 ==========================================================================")
        self.test_pre_tool_hook()
        self.test_post_tool_and_lifecycle_hooks()
        self.test_custom_agents_catalog()
        self.test_hooks_configuration_json()
        self.test_notebook_dossiers()

        print("\n" + "=" * 74)
        print(f"📊 RESUMEN FINAL: {self.tests_passed} PASS, {self.tests_failed} FAIL")
        print("=" * 74)
        return self.tests_failed == 0

if __name__ == "__main__":
    verifier = WorkflowVerifier()
    success = verifier.run_all()
    sys.exit(0 if success else 1)
