#!/usr/bin/env python3
"""
Upgrade All Verticals to Enterprise Hexagonal Architecture
----------------------------------------------------------
Itera sobre todos los proyectos en /home/jaruiz/Desarrollo/apps/ y aplica la
estructura hexagonal completa de nivel Staff/Principal (Java 25, Records, Ports & Adapters,
Properties Duales, Dockerfile Distroless, Cloudbuild SLSA L3 y Tests Zero-Mockito).
"""

import re
import sys
from pathlib import Path

# Añadir directorio de scaffolding al path
scaffolding_dir = Path(__file__).resolve().parent
sys.path.insert(0, str(scaffolding_dir))

from create_enterprise_project import create_enterprise_project

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
APPS_DIR = WORKSPACE_ROOT / "apps"

def infer_entity_name(project_name: str) -> str:
    clean = re.sub(r'^Proyecto', '', project_name)
    if not clean:
        clean = project_name
    return clean

def main():
    if not APPS_DIR.exists():
        print(f"Directorio no encontrado: {APPS_DIR}")
        sys.exit(1)

    vertical_dirs = [d for d in APPS_DIR.iterdir() if d.is_dir()]
    print(f"📦 Encontrados {len(vertical_dirs)} verticales en {APPS_DIR}.")

    upgraded = 0
    for v_dir in sorted(vertical_dirs):
        proj_name = v_dir.name
        entity_name = infer_entity_name(proj_name)
        description = f"Microservicio Vertical Especializado en {entity_name} para el Ecosistema Corporativo."
        
        try:
            create_enterprise_project(proj_name, entity_name, description)
            upgraded += 1
            print(f"  ✓ [{upgraded}/{len(vertical_dirs)}] {proj_name} actualizado con éxito.")
        except Exception as e:
            print(f"  ❌ Error actualizando {proj_name}: {e}")

    print(f"\n🎉 Todos los {upgraded} verticales han sido elevados al estándar hexagonal Pro-Grade.")

if __name__ == "__main__":
    main()
