"""
Arquitectura y especificación formal para fix_hooks.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import os
import re

repos = [
    "/home/jaruiz/Desarrollo/AppViajes",
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "/home/jaruiz/Desarrollo/SaaSRegantes",
]

for repo in repos:
    precommit_path = os.path.join(repo, ".git", "hooks", "pre-commit")
    if os.path.exists(precommit_path):
        with open(precommit_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        original = content
        # Update references to scripts
        content = content.replace('./scripts/start-cycle.sh', './scripts/bin/start-cycle.sh')
        content = content.replace('./scripts/sast-scanner.py', './scripts/bin/sast-scanner.py') # Just in case
        
        if content != original:
            with open(precommit_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Fixed pre-commit hook in {repo}")

