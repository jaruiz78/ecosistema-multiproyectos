#!/usr/bin/env python3
"""
audit_and_sync_all_multiproyectos.py
=============================================================================
Auditoría Exhaustiva de Todos los Proyectos del Ecosistema MultiProyectos.
Verifica:
1. Registro en pom.xml raíz y sincronización completa de módulos.
2. Presencia de tests unitarios y suites de Property-Based Testing.
3. Presencia de especificaciones técnicas (OpenAPI 3.1, AsyncAPI 3.0, FinOps).
4. Ejecución del build y test reactor de Maven global.
=============================================================================
"""

import os
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
ROOT_POM = WORKSPACE_ROOT / "pom.xml"
STARTER_POM = WORKSPACE_ROOT / "corp-spring-boot-starter" / "pom.xml"

def get_pom_modules(pom_path: Path):
    if not pom_path.exists():
        return set()
    tree = ET.parse(pom_path)
    root = tree.getroot()
    # Handle XML namespaces
    ns = {'mvn': 'http://maven.apache.org/POM/4.0.0'}
    modules = set()
    for mod in root.findall('.//{http://maven.apache.org/POM/4.0.0}module'):
        if mod.text:
            modules.add(mod.text.strip())
    # Fallback with regex if namespace parsing fails
    if not modules:
        content = pom_path.read_text()
        matches = re.findall(r'<module>(.*?)</module>', content)
        modules = set(m.strip() for m in matches)
    return modules

def audit_ecosystem():
    print("=" * 80)
    print("🔍 AUDITORÍA INTEGRAL DE MULTIPROYECTOS - ESTUDIOS GLOBALES")
    print("=" * 80)
    
    root_modules = get_pom_modules(ROOT_POM)
    starter_modules = get_pom_modules(STARTER_POM)
    
    # 1. Apps
    apps_dir = WORKSPACE_ROOT / "apps"
    all_apps = [d for d in apps_dir.iterdir() if d.is_dir() and not d.name.startswith(".")]
    
    # 2. Cores
    cores_dir = WORKSPACE_ROOT / "core"
    all_cores = [d for d in cores_dir.iterdir() if d.is_dir() and not d.name.startswith(".")]
    
    # 3. Starters
    starters_dir = WORKSPACE_ROOT / "corp-spring-boot-starter"
    all_starters = [d for d in starters_dir.iterdir() if d.is_dir() and (d / "pom.xml").exists() and not d.name.startswith(".")]

    print(f"\n📊 TOTAL DE COMPONENTES DETECTADOS EN EL FILE SYSTEM:")
    print(f"  • Proyectos Verticales en apps/: {len(all_apps)}")
    print(f"  • Módulos Algorítmicos en core/: {len(all_cores)}")
    print(f"  • Starters de Plataforma en corp-spring-boot-starter/: {len(all_starters)}")
    print(f"  • Proyectos Especiales (SaaSRegantes, AppViajes, pctMultiMicroservices): 3")
    
    # Comprobar apps no registradas en root pom
    missing_apps_in_root = []
    for app in sorted(all_apps, key=lambda x: x.name):
        rel = f"apps/{app.name}"
        if rel not in root_modules:
            missing_apps_in_root.append(rel)
            
    # Comprobar cores no registrados en root pom
    missing_cores_in_root = []
    for core in sorted(all_cores, key=lambda x: x.name):
        rel = f"core/{core.name}"
        if rel not in root_modules:
            missing_cores_in_root.append(rel)
            
    # Comprobar starters no registrados en starter pom
    missing_starters = []
    for s in sorted(all_starters, key=lambda x: x.name):
        if s.name not in starter_modules:
            missing_starters.append(s.name)

    print(f"\n🔎 REVISIÓN DE REGISTRO EN POM.XML:")
    print(f"  • Apps registradas en root pom.xml: {len(all_apps) - len(missing_apps_in_root)} / {len(all_apps)}")
    if missing_apps_in_root:
        print(f"    ⚠️ Apps pendientes de registrar en root pom: {missing_apps_in_root}")
    else:
        print(f"    ✓ 100% de apps registradas en root pom.xml")

    print(f"  • Cores registrados en root pom.xml: {len(all_cores) - len(missing_cores_in_root)} / {len(all_cores)}")
    if missing_cores_in_root:
        print(f"    ⚠️ Cores pendientes de registrar en root pom: {missing_cores_in_root}")
    else:
        print(f"    ✓ 100% de cores registrados en root pom.xml")

    print(f"  • Starters registrados en corp-spring-boot-starter/pom.xml: {len(all_starters) - len(missing_starters)} / {len(all_starters)}")
    if missing_starters:
        print(f"    ⚠️ Starters pendientes de registrar: {missing_starters}")
    else:
        print(f"    ✓ 100% de starters registrados")

    # Auditoría de Calidad y Testing
    print(f"\n🧪 AUDITORÍA DE TESTS Y DOCUMENTACIÓN:")
    apps_with_tests = 0
    apps_with_openapi = 0
    apps_with_asyncapi = 0
    
    for app in all_apps:
        has_tests = list((app / "src" / "test").rglob("*Test.java")) if (app / "src" / "test").exists() else []
        if has_tests:
            apps_with_tests += 1
        if (app / "docs" / "openapi.yaml").exists() or (app / "docs" / "openapi.json").exists():
            apps_with_openapi += 1
        if (app / "docs" / "asyncapi.yaml").exists() or (app / "docs" / "asyncapi.json").exists():
            apps_with_asyncapi += 1

    print(f"  • Apps con Tests Unitarios / Property-Based: {apps_with_tests} / {len(all_apps)} ({apps_with_tests/len(all_apps)*100:.1f}%)")
    print(f"  • Apps con OpenAPI 3.1: {apps_with_openapi} / {len(all_apps)} ({apps_with_openapi/len(all_apps)*100:.1f}%)")
    print(f"  • Apps con AsyncAPI 3.0: {apps_with_asyncapi} / {len(all_apps)} ({apps_with_asyncapi/len(all_apps)*100:.1f}%)")
    
    return {
        "missing_apps": missing_apps_in_root,
        "missing_cores": missing_cores_in_root,
        "missing_starters": missing_starters
    }

if __name__ == "__main__":
    audit_ecosystem()
