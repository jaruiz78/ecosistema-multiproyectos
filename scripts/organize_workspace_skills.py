#!/usr/bin/env python3
"""
organize_workspace_skills.py

Automated script to reorganize Antigravity skills and plugins across ~/.gemini/config
and workspace .agents/ directories to optimize token usage and avoid context budget truncation.
"""

import os
import sys
import shutil
import json

GLOBAL_SKILLS_DIR = os.path.expanduser('~/.gemini/config/skills')
GLOBAL_PLUGINS_DIR = os.path.expanduser('~/.gemini/config/plugins')
DESARROLLO_ROOT = '/home/jaruiz/Desarrollo'

# Core 10 SDLC skills to keep in ~/.gemini/config/skills/
CORE_HUB_SKILLS = {
    'spec-driven-development',
    'planning-and-task-breakdown',
    'incremental-implementation',
    'test-driven-development',
    'code-review-and-quality',
    'security-and-hardening',
    'debugging-and-error-recovery',
    'using-agent-skills',
    'context-engineering',
    'documentation-and-adrs'
}

# New Plugins to create in ~/.gemini/config/plugins/
NEW_PLUGINS = {
    'pack-java-spring': {
        'description': 'Paquete de Skills especializadas en Java 25, Leyden CDS, Spring Boot 4.0 y Arquitectura Hexagonal DDD',
        'skills': [
            'leyden-cds-trainer',
            'spring-boot4-native-check',
            'ddd-pure-domain-validator',
            'generate-hermetic-stubs'
        ]
    },
    'pack-python-twin': {
        'description': 'Paquete de Skills especializadas en Python, Simulaciones, Gemelo Digital Unificado, Vectorización NumPy y EnKF',
        'skills': [
            'python-vectorization-optimizer',
            'math-modeler',
            'unified-twin-node-injector',
            'genetic-algorithm-optimizer',
            'skill-mesa-abm-scaffold',
            'skill-pypsa-network-optimizer',
            'skill-sumo-traci-control',
            'simulation-telemetry-parser',
            'simulation-telemetry-sqlite-analyzer',
            'structured-json-generator',
            'python-memory-profiler'
        ]
    },
    'pack-go-backend': {
        'description': 'Paquete de Skills especializadas en Go, Microservicios de alta velocidad y Chaos Injector',
        'skills': [
            'go-benchmark-optimizer',
            'chaos-injector-script'
        ]
    },
    'pack-gcp-devsecops': {
        'description': 'Paquete de Skills especializadas en GCP, Multi-tenancy, BigQuery, Firestore Security Rules y SRE FinOps',
        'skills': [
            'multi_tenancy_security_specialist',
            'sre-finops-auditor',
            'bq-dry-run-optimizer',
            'bqml-pipeline-builder',
            'bigquery-graph',
            'bigquery-sql',
            'gcp-api-enabler',
            'gcloud-auth-verification',
            'data_bi_engineer',
            'resilience_integration_architect'
        ]
    }
}

# Project-specific skills to co-locate in workspace .agents/skills/
WORKSPACE_SKILL_MAPPINGS = {
    os.path.join(DESARROLLO_ROOT, 'AppViajes'): [
        'h3-surge-calculator',
        'agent-state-generator',
        'queue-bottleneck-detector'
    ],
    os.path.join(DESARROLLO_ROOT, 'corp-spring-boot-starter'): [
        'leyden-cds-trainer',
        'spring-boot4-native-check',
        'ddd-pure-domain-validator',
        'generate-hermetic-stubs'
    ],
    os.path.join(DESARROLLO_ROOT, 'SaaSRegantes'): [
        'multi_tenancy_security_specialist',
        'bq-dry-run-optimizer'
    ],
    os.path.join(DESARROLLO_ROOT, 'PCT/PCT_TASKS/pctMultiMicroservices'): [
        'go-benchmark-optimizer',
        'chaos-injector-script'
    ]
}

def ensure_dir(path):
    os.makedirs(path, exist_ok=True)

def organize():
    print("=== INICIANDO REORGANIZACIÓN ESTRUCTURAL DE SKILLS Y PLUGINS ===")
    
    # 1. Crear Plugins Temáticos
    for plugin_name, info in NEW_PLUGINS.items():
        plugin_dir = os.path.join(GLOBAL_PLUGINS_DIR, plugin_name)
        plugin_skills_dir = os.path.join(plugin_dir, 'skills')
        ensure_dir(plugin_skills_dir)
        
        # Write plugin.json
        plugin_json_path = os.path.join(plugin_dir, 'plugin.json')
        plugin_data = {
            "name": plugin_name,
            "version": "1.0.0",
            "description": info['description']
        }
        with open(plugin_json_path, 'w', encoding='utf-8') as f:
            json.dump(plugin_data, f, indent=2)
        print(f"[PLUGIN CREATIVO] {plugin_name} en {plugin_dir}")
        
        # Copy skills to plugin skills dir
        for sk in info['skills']:
            src = os.path.join(GLOBAL_SKILLS_DIR, sk)
            if os.path.exists(src):
                dst = os.path.join(plugin_skills_dir, sk)
                if os.path.exists(dst):
                    shutil.rmtree(dst)
                shutil.copytree(src, dst)
                print(f"  -> Empaquetada skill '{sk}' en plugin '{plugin_name}'")

    # 2. Co-localizar skills por Workspace
    for ws_path, skills in WORKSPACE_SKILL_MAPPINGS.items():
        if os.path.exists(ws_path):
            ws_skills_dir = os.path.join(ws_path, '.agents', 'skills')
            ensure_dir(ws_skills_dir)
            print(f"[WORKSPACE CO-LOCATION] {ws_path} -> {ws_skills_dir}")
            for sk in skills:
                src = os.path.join(GLOBAL_SKILLS_DIR, sk)
                if os.path.exists(src):
                    dst = os.path.join(ws_skills_dir, sk)
                    if os.path.exists(dst):
                        shutil.rmtree(dst)
                    shutil.copytree(src, dst)
                    print(f"  -> Co-localizada skill '{sk}' en workspace '{ws_path}'")

    # 3. Limpiar GLOBAL_SKILLS_DIR para mantener SOLO el Hub Core (10 skills)
    print("\n=== LIMPIANDO HUB GLOBAL DE SKILLS (~/.gemini/config/skills/) ===")
    if os.path.exists(GLOBAL_SKILLS_DIR):
        current_global = os.listdir(GLOBAL_SKILLS_DIR)
        moved_count = 0
        kept_count = 0
        for item in current_global:
            item_path = os.path.join(GLOBAL_SKILLS_DIR, item)
            if item.startswith('.'):
                continue
            if item in CORE_HUB_SKILLS:
                kept_count += 1
                print(f"  [CONSERVADA EN HUB CORE] {item}")
            else:
                # Remove from loose global skills directory because it's now in a plugin or workspace
                if os.path.isdir(item_path):
                    shutil.rmtree(item_path)
                else:
                    os.remove(item_path)
                moved_count += 1
                print(f"  [REMOVIDA DE GLOBAL LOOSE] {item}")
                
        print(f"\nResumen Global: {kept_count} conservadas en Hub Core, {moved_count} removidas de directorio suelto.")

def verify():
    print("\n=== VERIFICACIÓN DE ESTADO FINAL ===")
    if os.path.exists(GLOBAL_SKILLS_DIR):
        remaining = [s for s in os.listdir(GLOBAL_SKILLS_DIR) if not s.startswith('.')]
        print(f"Skills sueltas restantes en Hub Global ({len(remaining)}):")
        for r in sorted(remaining):
            print(f"  - {r}")
            
    print("\nPlugins activos en ~/.gemini/config/plugins/:")
    if os.path.exists(GLOBAL_PLUGINS_DIR):
        for p in sorted(os.listdir(GLOBAL_PLUGINS_DIR)):
            p_path = os.path.join(GLOBAL_PLUGINS_DIR, p)
            if os.path.isdir(p_path):
                sk_path = os.path.join(p_path, 'skills')
                sk_count = len(os.listdir(sk_path)) if os.path.exists(sk_path) else 0
                print(f"  - {p} ({sk_count} skills)")

if __name__ == '__main__':
    organize()
    verify()
