#!/usr/bin/env python3
"""
RUNNER MAESTRO DE PRUEBAS DE INTEGRACIÓN (REAL)
Valida la compilación y ejecución de tests unitarios de los 4 proyectos productivos
reales del ecosistema. Reemplaza a las simulaciones ficticias anteriores.
"""
import sys
import subprocess
from pathlib import Path

def color(text, code):
    return f"\033[{code}m{text}\033[0m"

def print_header(title):
    print(color(f"\n========================================================", "36"))
    print(color(f"  {title}", "1;36"))
    print(color(f"========================================================", "36"))

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")

def run_cmd(cmd: str, cwd: Path, project_name: str) -> bool:
    print(color(f"\n[Ejecutando en {project_name}] $ {cmd}", "33"))
    try:
        result = subprocess.run(cmd, shell=True, cwd=str(cwd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
        if result.returncode == 0:
            print(color(f"[PASSED] {project_name} -> {cmd}", "1;32"))
            return True
        else:
            print(color(f"[FAILED] {project_name} -> {cmd}", "1;31"))
            print(result.stdout)
            return False
    except Exception as e:
        print(color(f"[ERROR] {project_name} -> No se pudo ejecutar el comando: {e}", "1;31"))
        return False

def test_corp_spring_boot_starter():
    project = "corp-spring-boot-starter"
    cwd = WORKSPACE_ROOT / project
    if not cwd.exists():
        print(color(f"Directorio no encontrado: {cwd}", "31"))
        return False
    return run_cmd("mvn test", cwd, project)

def test_pct_multi_microservices():
    project = "pctMultiMicroservices"
    cwd = WORKSPACE_ROOT / "PCT/PCT_TASKS" / project
    if not cwd.exists():
        print(color(f"Directorio no encontrado: {cwd}", "31"))
        return False
    go_success = run_cmd("go test ./...", cwd / "services/bff-go", project + " (Go)")
    java_success = run_cmd("mvn test", cwd / "services/backend-java", project + " (Java)")
    return go_success and java_success

def test_saas_regantes():
    project = "SaaSRegantes"
    cwd = WORKSPACE_ROOT / project
    if not cwd.exists():
        print(color(f"Directorio no encontrado: {cwd}", "31"))
        return False
    frontend_cwd = cwd / "frontend"
    backend_success = run_cmd("mvn test", cwd, project)
    frontend_success = True
    if frontend_cwd.exists():
        frontend_success = run_cmd("npm install && npm run build", frontend_cwd, project + " (Frontend)")
    return backend_success and frontend_success

def test_app_viajes():
    project = "AppViajes"
    cwd = WORKSPACE_ROOT / project / "services" / "mobile-app"
    if not cwd.exists():
        print(color(f"Directorio no encontrado: {cwd}", "31"))
        return False
    return run_cmd("flutter test", cwd, project)

def main():
    print_header("SUITE MAESTRA DE PRUEBAS DEL ECOSISTEMA (PRODUCTIVO REAL)")
    
    results = [
        test_corp_spring_boot_starter(),
        test_pct_multi_microservices(),
        test_saas_regantes(),
        test_app_viajes()
    ]
    
    total = len(results)
    passed = sum(1 for r in results if r)
    
    print_header("RESUMEN DE RESULTADOS")
    print(f"Total Proyectos: {total}")
    print(color(f"PASSED: {passed}", "1;32"))
    
    if passed < total:
        print(color(f"FAILED: {total - passed}", "1;31"))
        sys.exit(1)
    else:
        print(color("¡Todos los proyectos compilan correctamente!", "1;32"))
        sys.exit(0)

if __name__ == "__main__":
    main()
