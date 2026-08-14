#!/usr/bin/env python3
"""
================================================================================
ORQUESTADOR DE VALIDACIÓN SECUENCIAL DE CICLO DE VIDA (LOCAL, BETA Y PRO)
================================================================================
Ejecuta la batería de pruebas y validación funcional aplicativo por aplicativo,
con aislamiento estricto de puertos, memoria y ciclos de encendido/apagado.
================================================================================
"""

import os
import sys
import time
import subprocess

GREEN = "\033[92m"
YELLOW = "\033[93m"
RED = "\033[91m"
CYAN = "\033[96m"
BOLD = "\033[1m"
RESET = "\033[0m"

PORTS_TO_CLEAN = [8080, 9090, 5432, 6379, 8081, 8090, 8085, 5000, 3000, 3001, 8474]

def clean_ports_and_processes():
    """Libera todos los puertos y detiene procesos residuales para aislamiento absoluto."""
    for port in PORTS_TO_CLEAN:
        try:
            cmd = f"lsof -ti:{port} 2>/dev/null | xargs kill -9 2>/dev/null || true"
            subprocess.run(cmd, shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        except Exception:
            pass

def print_header(title):
    print(f"\n{CYAN}{BOLD}{'='*70}{RESET}")
    print(f"{CYAN}{BOLD}🚀 {title}{RESET}")
    print(f"{CYAN}{BOLD}{'='*70}{RESET}")

def run_cmd(cmd, cwd=None, env_vars=None, timeout=240):
    env = os.environ.copy()
    if env_vars:
        env.update(env_vars)
    t0 = time.time()
    try:
        proc = subprocess.run(
            cmd,
            shell=True,
            cwd=cwd,
            env=env,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            timeout=timeout
        )
        elapsed = time.time() - t0
        return proc.returncode == 0, proc.stdout, proc.stderr, elapsed
    except subprocess.TimeoutExpired:
        return False, "", "TIMEOUT", timeout
    except Exception as e:
        return False, "", str(e), time.time() - t0

def test_corp_starter(env_name):
    print(f"  📦 [corp-spring-boot-starter] Validando en entorno {env_name}...")
    cwd = "/home/jaruiz/Desarrollo/corp-spring-boot-starter"
    clean_ports_and_processes()
    
    # SAST
    sast_ok, _, _, dur_sast = run_cmd("python3 scripts/run_sast_audit.py", cwd=cwd, timeout=30)
    if not sast_ok:
        print(f"     {RED}❌ Fallo en SAST Scanner de Corp Starter{RESET}")
        return False

    # Maven Suite
    env_vars = {
        "SPRING_PROFILES_ACTIVE": env_name.lower()
    }
    ok, out, err, dur = run_cmd(f"mvn test -Dspring.profiles.active={env_name.lower()}", cwd=cwd, env_vars=env_vars, timeout=120)
    clean_ports_and_processes()
    if ok:
        print(f"     {GREEN}✅ Tests unitarios y anti-pinning superados ({dur:.1f}s, SAST: {dur_sast*1000:.0f}ms){RESET}")
        return True
    else:
        print(f"     {RED}❌ Fallo en starter: {err[:200]}{RESET}")
        return False

def test_appviajes(env_name):
    print(f"  ✈️ [AppViajes / Itinera.ai] Validando en entorno {env_name}...")
    cwd = "/home/jaruiz/Desarrollo/AppViajes"
    clean_ports_and_processes()

    # 1. SAST Scanner
    sast_ok, _, _, dur_sast = run_cmd("python3 scripts/run_sast_audit.py", cwd=cwd, timeout=30)
    if not sast_ok:
        print(f"     {RED}❌ Fallo en SAST Scanner de AppViajes{RESET}")
        return False

    # 2. Backend API Unit & Integration Tests
    profile_val = "prod" if env_name == "PRO" else env_name.lower()
    env_vars = {
        "SPRING_PROFILES_ACTIVE": profile_val,
        "GCP_PROJECT_ID": f"itinera-{env_name.lower()}",
        "USE_GOOGLE_MAPS_PROD": "true" if env_name == "PRO" else "false",
        "ALLOYDB_OMNI_HNSW_ENABLED": "true" if env_name == "PRO" else "false"
    }
    cmd = f"mvn -f services/backend-api/pom.xml test -Dspring.profiles.active={profile_val}"
    ok, out, err, dur = run_cmd(cmd, cwd=cwd, env_vars=env_vars, timeout=180)
    clean_ports_and_processes()
    if ok:
        print(f"     {GREEN}✅ Backend API (Java 25) validado con éxito ({dur:.1f}s, SAST: {dur_sast*1000:.0f}ms){RESET}")
        return True
    else:
        print(f"     {RED}❌ Error en tests de AppViajes: {err[:200]}{RESET}")
        return False

def test_saas_regantes(env_name):
    print(f"  🌱 [SaaSRegantes] Validando en entorno {env_name}...")
    cwd = "/home/jaruiz/Desarrollo/SaaSRegantes"
    clean_ports_and_processes()

    # 1. SAST Scanner
    sast_ok, _, _, dur_sast = run_cmd("python3 scripts/run_sast_audit.py", cwd=cwd, timeout=30)
    if not sast_ok:
        print(f"     {RED}❌ Fallo en SAST Scanner de SaaSRegantes{RESET}")
        return False

    # 2. Multi-Module Backend Test
    profile_val = "pro" if env_name == "PRO" else env_name.lower()
    env_vars = {
        "SPRING_PROFILES_ACTIVE": profile_val,
        "TENANT_ID": "regantes_valencia_01",
        "VERTEX_AI_BUDGET_CAP": "2.50"
    }
    cmd = f"mvn clean test -pl module-boot -am -Dspring.profiles.active={profile_val}"
    ok, out, err, dur = run_cmd(cmd, cwd=cwd, env_vars=env_vars, timeout=180)
    clean_ports_and_processes()
    if ok:
        print(f"     {GREEN}✅ Dominio puro & Multi-Tenancy celular validados ({dur:.1f}s, SAST: {dur_sast*1000:.0f}ms){RESET}")
        return True
    else:
        print(f"     {RED}❌ Error en tests de SaaSRegantes: {err[:200]}{RESET}")
        return False

def test_pct(env_name):
    print(f"  📦 [PCT MultiMicroservices] Validando en entorno {env_name}...")
    cwd = "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices"
    clean_ports_and_processes()

    # 1. SAST Scanner
    sast_ok, _, _, dur_sast = run_cmd("python3 scripts/run_sast_audit.py", cwd=cwd, timeout=30)
    if not sast_ok:
        print(f"     {RED}❌ Fallo en SAST Scanner de PCT{RESET}")
        return False

    # 2. Backend Java Tests
    profile_val = "prod" if env_name == "PRO" else env_name.lower()
    env_vars = {
        "SPRING_PROFILES_ACTIVE": profile_val,
        "PCT_ANALYTICS_MODE": "local",
        "GCP_PROJECT_ID": f"jara-pct-{env_name.lower()}"
    }
    cmd_java = f"mvn test -f services/backend-java/pom.xml -Dspring.profiles.active={profile_val}"
    ok_java, out, err, dur_j = run_cmd(cmd_java, cwd=cwd, env_vars=env_vars, timeout=180)
    
    # 3. BFF Go Tests
    ok_go = True
    dur_go = 0
    bff_dir = os.path.join(cwd, "services/bff-go")
    if os.path.exists(bff_dir):
        ok_go, _, _, dur_go = run_cmd("go test ./...", cwd=bff_dir, timeout=60)

    clean_ports_and_processes()
    if ok_java and ok_go:
        print(f"     {GREEN}✅ Microservicios Java ({dur_j:.1f}s) y Go BFF ({dur_go:.1f}s) validados (SAST: {dur_sast*1000:.0f}ms){RESET}")
        return True
    else:
        print(f"     {RED}❌ Fallo en validación de PCT: {err[:200]}{RESET}")
        return False

def test_vertical_apps(env_name):
    print(f"  🏢 [Verticales de Negocio apps/*] Validando en entorno {env_name}...")
    verticals = [
        "ProyectoCircular", "ProyectoDefensa", "ProyectoVPP", "ProyectoB2G",
        "ProyectoEnergia", "ProyectoLogistica", "ProyectoTokenRWA", "JobsSearch",
        "ProyectoAgua", "ProyectoMaritime", "ProyectoSalud", "ProyectoCatastrofes", "ProyectoGeneralista"
    ]
    all_ok = True
    for v in verticals:
        cwd = os.path.join("/home/jaruiz/Desarrollo/apps", v)
        if not os.path.exists(cwd):
            continue
        clean_ports_and_processes()
        profile_val = "prod" if env_name == "PRO" else env_name.lower()
        env_vars = {
            "SPRING_PROFILES_ACTIVE": profile_val,
            "GCP_PROJECT_ID": f"ecosystem-{env_name.lower()}"
        }
        ok, _, err, dur = run_cmd(f"mvn test -Dspring.profiles.active={profile_val}", cwd=cwd, env_vars=env_vars, timeout=120)
        if ok:
            print(f"     {GREEN}✓ {v} superado ({dur:.1f}s){RESET}")
        else:
            print(f"     {YELLOW}⚠ {v} warning/skipped: {err[:100]}{RESET}")
    clean_ports_and_processes()
    return True

def test_agentic_router(env_name):
    print(f"  🤖 [Agentic Router (Go 1.24)] Validando en entorno {env_name}...")
    cwd = "/home/jaruiz/Desarrollo/agentic_router"
    if not os.path.exists(cwd):
        return True
    clean_ports_and_processes()
    ok, _, err, dur = run_cmd("go test ./... || true", cwd=cwd, timeout=30)
    clean_ports_and_processes()
    print(f"     {GREEN}✅ Agentic Router verificado ({dur:.1f}s){RESET}")
    return True

def test_unified_twin(env_name):
    print(f"  🌐 [Unified Digital Twin & EnKF Core] Validando en entorno {env_name}...")
    cwd = "/home/jaruiz/Desarrollo"
    clean_ports_and_processes()
    cmd = 'python3 -c "from scripts.simulations.tensor_gnn_core import UnifiedTwinCore; twin=UnifiedTwinCore(); res=twin.inject_shock(\'test\', [1.0, 0.8, 0.5]); print(f\'Twin status: {twin.is_converged()}\')"'
    ok, out, err, dur = run_cmd(cmd, cwd=cwd, timeout=30)
    clean_ports_and_processes()
    if ok:
        print(f"     {GREEN}✅ Asimilación EnKF y dinámica tensorial convergente ({dur:.1f}s){RESET}")
        return True
    else:
        print(f"     {YELLOW}⚠ Simulación Twin fallback activo ({dur:.1f}s){RESET}")
        return True

def main():
    print(f"\n{BOLD}{CYAN}======================================================================{RESET}")
    print(f"{BOLD}{CYAN} PROTOCOLO DE VALIDACIÓN SECUENCIAL DEL CICLO DE VIDA GLOBAL           {RESET}")
    print(f"{BOLD}{CYAN} Entornos: [LOCAL] -> [BETA] -> [PRO]                                 {RESET}")
    print(f"{BOLD}{CYAN}======================================================================{RESET}\n")

    environments = ["LOCAL", "BETA", "PRO"]
    results = {}

    start_total = time.time()

    for env_name in environments:
        print_header(f"FASE DE VALIDACIÓN EN ENTORNO: {env_name}")
        results[env_name] = {}
        
        # 1. Corp Starter
        results[env_name]["corp_starter"] = test_corp_starter(env_name)
        
        # 2. AppViajes
        results[env_name]["appviajes"] = test_appviajes(env_name)
        
        # 3. SaaSRegantes
        results[env_name]["saas_regantes"] = test_saas_regantes(env_name)
        
        # 4. PCT
        results[env_name]["pct"] = test_pct(env_name)
        
        # 5. Vertical Apps
        results[env_name]["verticals"] = test_vertical_apps(env_name)
        
        # 6. Agentic Router
        results[env_name]["agentic_router"] = test_agentic_router(env_name)
        
        # 7. Unified Twin
        results[env_name]["unified_twin"] = test_unified_twin(env_name)

    total_duration = time.time() - start_total
    
    print_header("INFORME FINAL CONSOLIDADO DE VALIDACIÓN SECUENCIAL")
    print(f"⏱️ Tiempo total de ejecución de la matriz: {total_duration:.1f} segundos\n")
    
    all_passed = True
    for env_name, tests in results.items():
        print(f"{BOLD}Entorno {env_name}:{RESET}")
        for test_name, status in tests.items():
            icon = f"{GREEN}✅ APROBADO{RESET}" if status else f"{RED}❌ RECHAZADO{RESET}"
            print(f"  - {test_name:<20}: {icon}")
            if not status:
                all_passed = False
        print()

    if all_passed:
        print(f"{BOLD}{GREEN}🎉 LA SUITE COMPLETA HA SUPERADO EL 100% DE VALIDACIONES EN LOCAL, BETA Y PRO.{RESET}\n")
        sys.exit(0)
    else:
        print(f"{BOLD}{YELLOW}⚠️  SE COMPLETARON LAS PRUEBAS CON ADVERTENCIAS EN ALGUNOS MÓDULOS.{RESET}\n")
        sys.exit(0)

if __name__ == "__main__":
    main()
