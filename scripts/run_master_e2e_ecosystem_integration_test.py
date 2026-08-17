#!/usr/bin/env python3
"""
RUNNER MAESTRO DE PRUEBAS DE INTEGRACIÓN DEL ECOSISTEMA (REAL & EXHAUSTIVO)
=============================================================================
Valida la integridad de extremo a extremo en 4 ejes fundamentales:
1. Proyectos Productivos Base (corp-spring-boot-starter, pctMultiMicroservices, SaaSRegantes, AppViajes).
2. Cores Matemáticos y Físicos (core-kalman-twin, core-spatial-h3-3d, core-quantum-mesh, etc.).
3. Verticales Especializados (Dominio Hexagonal Puro & Zero-Mockito).
4. Asimilación del Gemelo Digital y Consilium Romano 3.0.
"""

import os
import sys
import subprocess
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")

def color(text, code):
    return f"\033[{code}m{text}\033[0m"

def print_header(title):
    print(color(f"\n========================================================", "36"))
    print(color(f"  {title}", "1;36"))
    print(color(f"========================================================", "36"))

def run_cmd(cmd: str, cwd: Path, name: str) -> bool:
    print(color(f"\n[Ejecutando en {name}] $ {cmd}", "33"))
    try:
        result = subprocess.run(cmd, shell=True, cwd=str(cwd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
        if result.returncode == 0:
            print(color(f"[PASSED] {name} -> {cmd}", "1;32"))
            return True
        else:
            print(color(f"[FAILED] {name} -> {cmd}", "1;31"))
            print(result.stdout[:800] if result.stdout else "Sin salida")
            return False
    except Exception as e:
        print(color(f"[ERROR] {name} -> Excepción: {e}", "1;31"))
        return False

def test_corp_spring_boot_starter():
    project = "corp-spring-boot-starter"
    cwd = WORKSPACE_ROOT / project
    if not cwd.exists():
        return False
    return run_cmd("mvn test -q", cwd, project + " (Java 25 Loom)")

def test_pct_multi_microservices():
    project = "pctMultiMicroservices"
    cwd = WORKSPACE_ROOT / "PCT/PCT_TASKS" / project
    if not cwd.exists():
        return False
    go_success = run_cmd("go test -run=Test ./...", cwd / "services/bff-go", project + " (Go BFF)")
    return go_success

def test_saas_regantes():
    project = "SaaSRegantes"
    cwd = WORKSPACE_ROOT / project
    if not cwd.exists():
        return False
    return run_cmd("mvn test -q", cwd, project + " (Multi-Tenant)")

def test_app_viajes():
    project = "AppViajes"
    cwd = WORKSPACE_ROOT / project / "services" / "mobile-app"
    if not cwd.exists():
        return False
    return run_cmd("flutter analyze", cwd, project)

def test_python_cores():
    print_header("EVALUACIÓN DE CORES MATEMÁTICOS & FÍSICOS (PYTHON)")
    cores = [
        "core-kalman-twin",
        "core-spatial-h3-3d",
        "core-quantum-mesh",
        "core-causal-inference",
        "core-federated-privacy",
        "core-interstellar-mesh"
    ]
    passed_cores = 0
    for c in cores:
        core_dir = WORKSPACE_ROOT / "core" / c
        if core_dir.exists():
            success = run_cmd(f"pytest {core_dir}/tests -q", WORKSPACE_ROOT, f"Core ({c})")
            if success:
                passed_cores += 1
    return passed_cores == len(cores)

def test_consilium_simulations():
    print_header("AUDITORÍA DE ASIMILACIÓN Y GEMELO DIGITAL (CONSILIUM ROMANO 3.0)")
    cmd = "python3 scripts/consilium_romano_tribunal.py --audit-simulations"
    return run_cmd(cmd, WORKSPACE_ROOT, "Consilium Romano Telemetry EnKF")

def test_rag_knowledge_and_katas():
    print_header("VALIDACIÓN DE BASE DE CONOCIMIENTO RAG Y KATAS FEYNMAN")
    import sqlite3
    try:
        conn = sqlite3.connect(WORKSPACE_ROOT / "data/simulations_telemetry.db")
        c = conn.cursor()
        total_nodes = c.execute("SELECT COUNT(*) FROM university_knowledge_nodes").fetchone()[0]
        katas = c.execute("SELECT COUNT(*) FROM university_knowledge_nodes WHERE category='KATA_MAESTRA_FEYNMAN'").fetchone()[0]
        adrs = c.execute("SELECT COUNT(*) FROM university_knowledge_nodes WHERE category='ADR_DECISION'").fetchone()[0]
        conn.close()

        print(color(f"  • Total Nodos RAG: {total_nodes} (esperado > 180)", "32" if total_nodes >= 180 else "31"))
        print(color(f"  • Katas Maestras Feynman: {katas}/12 indexadas", "32" if katas == 12 else "31"))
        print(color(f"  • Decisiones ADR: {adrs} indexadas", "32" if adrs >= 10 else "31"))

        return total_nodes >= 180 and katas == 12
    except Exception as e:
        print(color(f"Error verificando RAG en SQLite: {e}", "31"))
        return False

def test_strategic_verticals():
    print_header("VALIDACIÓN DE VERTICALES HEXAGONALES (JAVA 25 & ZERO-MOCKITO)")
    strategic = ["ProyectoB2G", "ProyectoEnergia", "ProyectoVPP", "ProyectoLogistica", "ProyectoTokenRWA"]
    passed = 0
    for v in strategic:
        v_dir = WORKSPACE_ROOT / "apps" / v
        if v_dir.exists():
            success = run_cmd(f"mvn test -q -f {v_dir}/pom.xml", WORKSPACE_ROOT, f"Vertical {v}")
            if success:
                passed += 1
    return passed == len(strategic)

def test_high_fidelity_realism_suite():
    print_header("REALISMO 100%: INGESTA NO-GAUSSIANA, MLOps CONTINUO, RESILIENCIA Y JUEGOS NASH")
    cmd = "python3 scripts/simulations/run_100pct_realistic_simulation_suite.py"
    return run_cmd(cmd, WORKSPACE_ROOT, "100% Realistic High-Fidelity Simulation Suite")

def main():
    print_header("SUITE MAESTRA DE PRUEBAS DEL ECOSISTEMA (PRODUCTIVO REAL)")

    results = {
        "corp-spring-boot-starter (Java 25)": test_corp_spring_boot_starter(),
        "pctMultiMicroservices (Go BFF)": test_pct_multi_microservices(),
        "SaaSRegantes (Multi-Tenant)": test_saas_regantes(),
        "AppViajes (Flutter / H3)": test_app_viajes(),
        "Cores Matemáticos / Físicos (Python)": test_python_cores(),
        "Consilium Romano 3.0 & EnKF Covariance": test_consilium_simulations(),
        "RAG Knowledge Engine & 12 Feynman Katas": test_rag_knowledge_and_katas(),
        "Verticales Hexagonales Pro-Grade (Java 25)": test_strategic_verticals(),
        "Gemelo Digital 100% Realista (MLOps & CIP)": test_high_fidelity_realism_suite(),
    }

    print_header("RESUMEN DE RESULTADOS MAESTROS")
    total = len(results)
    passed = 0
    for name, status in results.items():
        st_text = color("PASSED", "1;32") if status else color("FAILED", "1;31")
        print(f" • {name.ljust(44)}: [{st_text}]")
        if status:
            passed += 1

    print(f"\nTotal Componentes: {total}")
    print(color(f"Aprobados: {passed}/{total}", "1;32" if passed == total else "1;31"))

    if passed < total:
        print(color("\n❌ Fallos detectados en la suite de pruebas.", "1;31"))
        sys.exit(1)
    else:
        print(color("\n🎉 ¡SUITE MAESTRA COMPLETAMENTE VERDE! Ecosistema en Nivel Staff/Principal (9.5+).", "1;32"))
        sys.exit(0)

if __name__ == "__main__":
    main()
