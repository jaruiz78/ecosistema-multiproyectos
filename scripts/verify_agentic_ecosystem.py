#!/usr/bin/env python3
"""
Arquitectura y especificación formal para verify_agentic_ecosystem.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
"""
verify_agentic_ecosystem.py

Script de verificación para auditar la integridad del Dream Team 2.0 (14 Roles Agénticos),
la presencia de las 5 nuevas SKILL.md y la consistencia de los archivos AGENTS.md.
"""

import os
import sys

DESARROLLO_ROOT = '/home/jaruiz/Desarrollo'
GLOBAL_AGENTS_FILE = '/home/jaruiz/.gemini/config/AGENTS.md'
GLOBAL_PLUGINS_DIR = '/home/jaruiz/.gemini/config/plugins'

EXPECTED_ROLES = [
    'Platform-DevSecOps-Architect',
    'Java-Spring-Expert',
    'Go-Gopher',
    'Frontend-Wizard',
    'Mobile-Mobility-Architect',
    'QA-Automation-Loop',
    'Zero-Trust-Security-Auditor',
    'Stripe-Fintech-Engineer',
    'Unified-Twin-Architect',
    'Leyden-AOT-Build-Master',
    'Zero-Mockito-TDD-Engineer',
    'Digital-Twin-EnKF-Orchestrator',
    'SLSA-Sigstore-Release-Sentinel',
    'ADR-Knowledge-Graph-Curator'
]

NEW_SKILL_PATHS = [
    os.path.join(GLOBAL_PLUGINS_DIR, 'pack-java-spring/skills/leyden-aot-build-master/SKILL.md'),
    os.path.join(GLOBAL_PLUGINS_DIR, 'pack-java-spring/skills/zero-mockito-tdd-engineer/SKILL.md'),
    os.path.join(GLOBAL_PLUGINS_DIR, 'pack-python-twin/skills/digital-twin-enkf-orchestrator/SKILL.md'),
    os.path.join(GLOBAL_PLUGINS_DIR, 'pack-gcp-devsecops/skills/slsa-sigstore-release-sentinel/SKILL.md'),
    os.path.join(GLOBAL_PLUGINS_DIR, 'pack-gcp-devsecops/skills/adr-knowledge-graph-curator/SKILL.md')
]

PROJECT_AGENTS_FILES = [
    os.path.join(DESARROLLO_ROOT, 'AppViajes/AGENTS.md'),
    os.path.join(DESARROLLO_ROOT, 'SaaSRegantes/AGENTS.md'),
    os.path.join(DESARROLLO_ROOT, 'corp-spring-boot-starter/AGENTS.md'),
    os.path.join(DESARROLLO_ROOT, 'PCT/PCT_TASKS/pctMultiMicroservices/AGENTS.md')
]

def verify():
    print("=== AUDITORÍA DEL ECOSISTEMA AGÉNTICO 2.0 ===")
    errors = 0
    
    # 1. Verificar global AGENTS.md
    if os.path.exists(GLOBAL_AGENTS_FILE):
        with open(GLOBAL_AGENTS_FILE, 'r', encoding='utf-8') as f:
            content = f.read()
        print(f"[OK] AGENTS.md global encontrado ({len(content)} bytes)")
        for role in EXPECTED_ROLES:
            if role in content:
                print(f"  -> Rol '{role}' verificado en global AGENTS.md")
            else:
                print(f"  [ERROR] Rol '{role}' no encontrado en global AGENTS.md")
                errors += 1
    else:
        print(f"[ERROR] No existe {GLOBAL_AGENTS_FILE}")
        errors += 1

    # 2. Verificar existencia de las 5 nuevas SKILL.md
    print("\n=== VERIFICACIÓN DE ARCHIVOS SKILL.MD NUEVOS ===")
    for path in NEW_SKILL_PATHS:
        if os.path.exists(path):
            print(f"[OK] SKILL.md verificado: {os.path.basename(os.path.dirname(path))}")
        else:
            print(f"[ERROR] Archivo no existe: {path}")
            errors += 1

    # 3. Verificar AGENTS.md de proyectos
    print("\n=== VERIFICACIÓN DE PROYECTOS MULTIPROYECTOS ===")
    for proj_agents in PROJECT_AGENTS_FILES:
        if os.path.exists(proj_agents):
            with open(proj_agents, 'r', encoding='utf-8') as f:
                c = f.read()
            if 'slsa-sigstore-release-sentinel' in c and 'leyden-aot-build-master' in c:
                print(f"[OK] Proyecto actualizado: {os.path.relpath(proj_agents, DESARROLLO_ROOT)}")
            else:
                print(f"[WARN] Proyecto {os.path.relpath(proj_agents, DESARROLLO_ROOT)} falta de dispatch 6-phase")
                errors += 1
        else:
            print(f"[ERROR] Archivo no encontrado: {proj_agents}")
            errors += 1

    print("\n" + "="*50)
    if errors == 0:
        print("RESULTADO: 100% CONSISTENTE. Ecosistema Agéntico 2.0 verificado exitosamente.")
        sys.exit(0)
    else:
        print(f"RESULTADO: Se encontraron {errors} errores en la verificación.")
        sys.exit(1)

if __name__ == '__main__':
    verify()
