"""
Arquitectura y especificación formal para analyze_cleanliness.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import os
import re
import json

repos = {
    "AppViajes": "/home/jaruiz/Desarrollo/AppViajes",
    "pctMultiMicroservices": "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "SaaSRegantes": "/home/jaruiz/Desarrollo/SaaSRegantes",
    "corp-spring-boot-starter": "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
}

def analyze_gitignore(repo_path):
    gitignore_path = os.path.join(repo_path, ".gitignore")
    issues = []
    if not os.path.exists(gitignore_path):
        return ["Missing .gitignore"]
    
    with open(gitignore_path, 'r') as f:
        content = f.read()
        
    lines = [l.strip() for l in content.split('\n')]
    
    # Check for basic python ignores if python files exist
    has_python = False
    has_java = False
    
    for root, dirs, files in os.walk(repo_path):
        if '.git' in dirs: dirs.remove('.git')
        if 'node_modules' in dirs: dirs.remove('node_modules')
        for file in files:
            if file.endswith('.py'): has_python = True
            if file.endswith('.java'): has_java = True
            
    if has_python:
        if '__pycache__/' not in lines: issues.append("Missing __pycache__/ in gitignore")
        if '*.pyc' not in lines: issues.append("Missing *.pyc in gitignore")
        if 'venv/' not in lines and '**/venv/' not in lines and '.venv/' not in lines: 
            issues.append("Missing venv/ in gitignore")
            
    if has_java:
        if 'target/' not in lines and '**/target/' not in lines: issues.append("Missing target/ in gitignore")
        if '*.class' not in lines: issues.append("Missing *.class in gitignore")
        
    if '.gitignore' in lines:
        issues.append(".gitignore file is ignoring itself (.gitignore found in rules)")
        
    return issues

def analyze_code_cleanliness(repo_path):
    metrics = {
        "large_files": 0,
        "todos": 0,
        "prints_in_python": 0,
        "empty_blocks": 0
    }
    
    for root, dirs, files in os.walk(repo_path):
        if '.git' in dirs: dirs.remove('.git')
        if 'node_modules' in dirs: dirs.remove('node_modules')
        if 'venv' in dirs: dirs.remove('venv')
        if '.venv' in dirs: dirs.remove('.venv')
        if '__pycache__' in dirs: dirs.remove('__pycache__')
        if 'target' in dirs: dirs.remove('target')
        
        for file in files:
            if file.endswith(('.py', '.java', '.go', '.dart', '.ts')):
                filepath = os.path.join(root, file)
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        lines = f.readlines()
                except UnicodeDecodeError:
                    continue
                
                if len(lines) > 500:
                    metrics["large_files"] += 1
                    
                content = "".join(lines)
                
                # Check TODOs
                metrics["todos"] += len(re.findall(r'(?i)#\s*todo|//\s*todo|/\*\s*todo', content))
                
                # Check empty catch/except
                metrics["empty_blocks"] += len(re.findall(r'catch\s*\([^\)]+\)\s*{\s*}|except[^:]*:\s*pass', content))
                
                if file.endswith('.py') and not file.endswith('_test.py') and not 'test_' in file:
                    metrics["prints_in_python"] += len(re.findall(r'print\(', content))
                    
    return metrics

report = {}
for name, path in repos.items():
    print(f"Analyzing {name}...")
    report[name] = {
        "gitignore_issues": analyze_gitignore(path),
        "code_metrics": analyze_code_cleanliness(path)
    }
    
with open('/home/jaruiz/Desarrollo/cleanliness_report.json', 'w') as f:
    json.dump(report, f, indent=4)
print("Report generated.")
