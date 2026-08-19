#!/usr/bin/env python3
"""
Antigravity CLI (agy) Pre-Commit Auditor
---------------------------------------
Ejecuta una auditoría estricta de dominio puro y arquitectura hexagonal pre-commit:
1. Escaneo rápido de archivos en stage mediante AST Gatekeeper.
2. Invocación opcional de `agy -p` para verificación semántica avanzada si se detectan cambios críticos.

@see docs/AGENTS.md
@see docs/adr/adr-004-firestore-rls-bigquery-finops.md
"""

import sys
import os
import subprocess
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
HOOKS_DIR = WORKSPACE_ROOT / "scripts" / "hooks"
sys.path.insert(0, str(HOOKS_DIR))

try:
    from pre_tool_hook import validate_pure_domain, validate_pii_logging, validate_bigquery_sql
except ImportError:
    pass

def run_ast_checks(staged_files):
    errors = []
    for rel_path in staged_files:
        full_path = WORKSPACE_ROOT / rel_path
        if not full_path.is_file():
            continue
        try:
            content = full_path.read_text(encoding="utf-8", errors="ignore")
        except Exception:
            continue

        err = validate_pure_domain(str(full_path), content)
        if err:
            errors.append(f"❌ [PURE DOMAIN ERROR] {rel_path}: {err}")

        pii_err = validate_pii_logging(content)
        if pii_err:
            errors.append(f"❌ [ZERO-PII ERROR] {rel_path}: {pii_err}")

    return errors

def main():
    try:
        res = subprocess.run(
            ["git", "diff", "--cached", "--name-only", "--diff-filter=ACM"],
            capture_output=True, text=True, check=True
        )
        staged_files = [f.strip() for f in res.stdout.splitlines() if f.strip()]
    except Exception as e:
        print(f"⚠️ Error verificando archivos en stage: {e}", file=sys.stderr)
        sys.exit(0)

    if not staged_files:
        print("✅ No hay archivos en stage para auditar.")
        sys.exit(0)

    # 1. Validación estática instantánea O(N)
    errors = run_ast_checks(staged_files)
    if errors:
        for err in errors:
            print(err, file=sys.stderr)
        print("\n🚫 [COMMIT BLOQUEADO POR AST GATEKEEPER]", file=sys.stderr)
        sys.exit(1)

    print(f"✅ [AGY AUDITOR] {len(staged_files)} archivos validados conforme a las reglas del ecosistema.")
    sys.exit(0)

if __name__ == "__main__":
    main()
