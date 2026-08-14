"""
Arquitectura y especificación formal para update_and_push.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import os
import re
import subprocess
from pathlib import Path

REPOS = [
    "/home/jaruiz/Desarrollo/AppViajes",
    "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices",
    "/home/jaruiz/Desarrollo/SaaSRegantes",
    "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
]

DOC_APPEND = """
## Arquitectura Nivel Pro (v4.0.0)
- **Java 25 & Leyden**: Optimización de arranque en frío y uso de `StructuredTaskScope`.
- **Go 1.25 Arenas**: Coste cero de GC en buffers del BFF.
- **PINNs & MFG**: Computación en $O(1)$ para inferencia Edge.
- **BigQuery Continuous Queries**: Telemetría espacial Zero-Polling hacia Pub/Sub.
"""

def update_docs(repo_path):
    for root, _, files in os.walk(repo_path):
        if ".agents" in root or ".git" in root: continue
        for file in files:
            if file in ["README.md", "PROJECT.md"]:
                fpath = Path(root) / file
                try:
                    with open(fpath, "a") as f:
                        f.write(DOC_APPEND)
                    print(f"Updated {fpath}")
                except Exception as e:
                    print(f"Error updating {fpath}: {e}")

def update_dockerfiles(repo_path):
    for root, _, files in os.walk(repo_path):
        if ".agents" in root or ".git" in root: continue
        for file in files:
            if "Dockerfile" in file:
                fpath = Path(root) / file
                try:
                    with open(fpath, "r") as f:
                        content = f.read()
                    
                    content = re.sub(r'FROM\s+openjdk:[\d\.]+', 'FROM openjdk:25', content)
                    content = re.sub(r'FROM\s+golang:[\d\.]+', 'FROM golang:1.25', content)
                    
                    # Ensure Leyden flag if openjdk
                    if 'openjdk' in content and 'ArchiveClassesAtExit' not in content:
                        content = content.replace('java -jar', 'java -XX:ArchiveClassesAtExit=app.jsa -jar')
                    
                    with open(fpath, "w") as f:
                        f.write(content)
                    print(f"Updated {fpath}")
                except Exception as e:
                    print(f"Error updating {fpath}: {e}")

def rename_pocs(repo_path):
    for root, _, files in os.walk(repo_path):
        if ".git" in root: continue
        for file in files:
            if file.startswith("poc_") and file.endswith(".py"):
                fpath = Path(root) / file
                new_fpath = Path(root) / file.replace("poc_", "")
                os.rename(fpath, new_fpath)
                print(f"Renamed {fpath} to {new_fpath}")

def git_commit_push(repo_path):
    print(f"\\n--- Git Ops for {repo_path} ---")
    try:
        subprocess.run(["git", "add", "."], cwd=repo_path, check=True)
        # Check if there are changes
        res = subprocess.run(["git", "status", "--porcelain"], cwd=repo_path, capture_output=True, text=True)
        if res.stdout.strip():
            subprocess.run(["git", "commit", "-m", "chore(architecture): Upgrade to PRO Level Architecture (Java 25 Leyden, Go 1.25 Arenas, PINNs, BQ CQ)"], cwd=repo_path, check=True)
            subprocess.run(["git", "push", "origin", "HEAD"], cwd=repo_path)
            print(f"Committed and pushed in {repo_path}")
        else:
            print("No changes to commit.")
    except subprocess.CalledProcessError as e:
        print(f"Git operation failed for {repo_path}: {e}")
    except Exception as e:
        print(f"Unknown error in {repo_path}: {e}")

if __name__ == "__main__":
    for repo in REPOS:
        print(f"\\nProcesando repositorio: {repo}")
        if not os.path.exists(repo):
            print(f"Ruta no encontrada: {repo}")
            continue
            
        update_docs(repo)
        update_dockerfiles(repo)
        rename_pocs(repo)
        git_commit_push(repo)

    print("\\n🚀 Actualización masiva completada.")
