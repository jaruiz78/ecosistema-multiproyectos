#!/usr/bin/env python3
"""
Batch Markdown, Mermaid and KaTeX Syntax Linter & Auto-Fixer
------------------------------------------------------------
Escanea y corrige automáticamente errores frecuentes de renderizado en archivos Markdown:
1. Escape de símbolos de moneda ($100 -> `$100`).
2. Entrecomillado de etiquetas Mermaid con caracteres especiales (id[Texto (info)] -> id["Texto (info)"]).
3. Normalización de separadores de tablas.

@see docs/AGENTS.md
"""

import sys
import re
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
HOOKS_DIR = WORKSPACE_ROOT / "scripts" / "hooks"
sys.path.insert(0, str(HOOKS_DIR))

from syntax_validator import validate_all

def fix_content(text: str) -> str:
    """Aplica transformaciones seguras para corregir errores de renderizado."""
    lines = text.splitlines()
    fixed_lines = []
    in_mermaid = False

    for line in lines:
        if line.strip().startswith("```mermaid"):
            in_mermaid = True
            fixed_lines.append(line)
            continue
        elif in_mermaid and line.strip().startswith("```"):
            in_mermaid = False
            fixed_lines.append(line)
            continue

        if in_mermaid:
            # Corregir etiquetas con caracteres especiales sin comillas: id[Algo (con) parentesis]
            # Convertir a id["Algo (con) parentesis"]
            fixed_line = re.sub(
                r'([a-zA-Z0-9_\-]+)\s*\[([^"\'\]]+[\(\)\[\]\{\}:/&|<>][^"\'\]]*)\]',
                r'\1["\2"]',
                line
            )
            fixed_lines.append(fixed_line)
        else:
            # Corregir moneda no escapada ni envuelta en backticks fuera de código
            # e.g. $100 -> `$100` si no está precedido por ` o \ o $
            fixed_line = re.sub(
                r'(?<![`\\\$])\$([0-9]+(?:\.[0-9]+)?(?:\s*USD|\s*EUR|\s*/MAU|\s*/kWh|\b))',
                r'`$\1`',
                line
            )
            fixed_lines.append(fixed_line)

    return "\n".join(fixed_lines) + ("\n" if text.endswith("\n") else "")

def scan_and_fix(directory: Path, auto_fix: bool = False):
    md_files = list(directory.glob("**/*.md"))
    print(f"🔍 Escaneando {len(md_files)} archivos Markdown en {directory}...")
    
    files_with_errors = 0
    total_errors = 0

    for md_file in md_files:
        # Excluir node_modules o build artifacts
        if "node_modules" in str(md_file) or "target" in str(md_file) or ".git" in str(md_file):
            continue

        try:
            content = md_file.read_text(encoding="utf-8", errors="ignore")
        except Exception:
            continue

        errors = validate_all(content)
        if errors:
            files_with_errors += 1
            total_errors += len(errors)
            print(f"\n📄 {md_file.relative_to(WORKSPACE_ROOT)} ({len(errors)} errores):")
            for err in errors[:3]:
                print(f"   • {err}")
            if len(errors) > 3:
                print(f"   • ... y {len(errors) - 3} errores adicionales.")

            if auto_fix:
                fixed = fix_content(content)
                remaining_errs = validate_all(fixed)
                if len(remaining_errs) < len(errors):
                    md_file.write_text(fixed, encoding="utf-8")
                    print(f"   ✅ Auto-corregido: {len(errors) - len(remaining_errs)} errores subsanados.")

    print("\n" + "=" * 60)
    print(f"📊 RESUMEN: {files_with_errors} archivos con incidencias ({total_errors} errores totales).")
    print("=" * 60)

if __name__ == "__main__":
    fix_mode = "--fix" in sys.argv
    target_path = Path(sys.argv[1]) if len(sys.argv) > 1 and not sys.argv[1].startswith("--") else (WORKSPACE_ROOT / "docs")
    scan_and_fix(target_path, auto_fix=fix_mode)
