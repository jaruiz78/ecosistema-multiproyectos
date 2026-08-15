"""
Arquitectura y especificación formal para phase2_empty_blocks.py.

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
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
]

java_catch = re.compile(r'(catch\s*\(\s*[A-Za-z0-9_.]+\s+([A-Za-z0-9_]+)\s*\)\s*{\s*})')
python_except = re.compile(r'(except\s*(?:[A-Za-z0-9_.]+)?(?:\s*as\s*[A-Za-z0-9_]+)?\s*:\s*pass)')

def refactor_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except UnicodeDecodeError:
        return
        
    original = content
    
    if filepath.endswith('.py'):
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if re.search(r'except[^:]*:\s*pass', line):
                indent = line[:len(line) - len(line.lstrip())]
                lines[i] = f"{indent}except Exception as e:\n{indent}    import logging\n{indent}    logging.error('Swallowed exception', exc_info=True)"
        content = '\n'.join(lines)
        
    elif filepath.endswith(('.java', '.ts', '.dart', '.js')):
        def java_repl(match):
            var_name = match.group(2)
            if filepath.endswith('.java'):
                return match.group(1).replace('{', f'{{ System.err.println("[TELEMETRY] Swallowed exception: " + {var_name}.getMessage());')
            elif filepath.endswith(('.ts', '.js')):
                return match.group(1).replace('{', f'{{ console.error("[TELEMETRY] Swallowed exception", {var_name});')
            elif filepath.endswith('.dart'):
                return match.group(1).replace('{', f'{{ print("[TELEMETRY] Swallowed exception: ${var_name}");')
            return match.group(0)
            
        content = java_catch.sub(java_repl, content)
        
    if original != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Refactored empty blocks in {filepath}")

for repo in repos:
    for root, dirs, files in os.walk(repo):
        if '.git' in dirs: dirs.remove('.git')
        if 'node_modules' in dirs: dirs.remove('node_modules')
        if 'venv' in dirs: dirs.remove('venv')
        if '.venv' in dirs: dirs.remove('.venv')
        
        for file in files:
            if file.endswith(('.py', '.java', '.ts', '.dart', '.js')):
                refactor_file(os.path.join(root, file))

print("Phase 2 complete.")
