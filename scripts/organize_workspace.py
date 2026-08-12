#!/usr/bin/env python3
import os
import shutil
import glob

BASE_DIR = "/home/jaruiz/Desarrollo"

# Carpetas destino
APPS_DIR = os.path.join(BASE_DIR, "apps")
CORE_DIR = os.path.join(BASE_DIR, "core")
SCRIPTS_DIR = os.path.join(BASE_DIR, "scripts")
DATA_DIR = os.path.join(BASE_DIR, "data")
DOCS_DIR = os.path.join(BASE_DIR, "docs")

# Subcarpetas para scripts
SCRIPTS_SIM = os.path.join(SCRIPTS_DIR, "simulations")
SCRIPTS_SCAFFOLD = os.path.join(SCRIPTS_DIR, "scaffolding")
SCRIPTS_CICD = os.path.join(SCRIPTS_DIR, "ci_cd")
SCRIPTS_ANALYSIS = os.path.join(SCRIPTS_DIR, "analysis")

def makedirs():
    dirs = [
        APPS_DIR, CORE_DIR, DATA_DIR, DOCS_DIR,
        SCRIPTS_SIM, SCRIPTS_SCAFFOLD, SCRIPTS_CICD, SCRIPTS_ANALYSIS
    ]
    for d in dirs:
        os.makedirs(d, exist_ok=True)

def move_if_exists(src_name, dest_folder):
    src = os.path.join(BASE_DIR, src_name)
    if os.path.exists(src):
        try:
            shutil.move(src, os.path.join(dest_folder, os.path.basename(src)))
            print(f"✅ Moved {src_name} -> {dest_folder}")
        except Exception as e:
            print(f"❌ Error moving {src_name}: {e}")

def organize_apps():
    apps = [
        "ProyectoB2G", "ProyectoCircular", "ProyectoDefensa", 
        "ProyectoEnergia", "ProyectoLogistica", "ProyectoTokenRWA", 
        "ProyectoVPP", "JobsSearch"
    ]
    for app in apps:
        move_if_exists(app, APPS_DIR)

def organize_core():
    cores = ["core-geogrid-h3", "core-kalman-twin", "core-govtech-ledger"]
    for c in cores:
        move_if_exists(c, CORE_DIR)

def organize_data():
    move_if_exists("models", DATA_DIR)
    move_if_exists("simulations_telemetry.db", DATA_DIR)
    move_if_exists("current_state.json", DATA_DIR)
    move_if_exists("cleanliness_report.json", DATA_DIR)
    # Move logs
    for log in glob.glob(os.path.join(BASE_DIR, "*.log")):
        move_if_exists(os.path.basename(log), DATA_DIR)

def organize_docs():
    docs = ["AGENTS.md", "PROJECT.md", "FINAL_OPTIMIZATION_REPORT.md", "ORIGINAL_REQUEST.md", "Git.txt"]
    for doc in docs:
        move_if_exists(doc, DOCS_DIR)
    
    # Mover formacion / docs de corp-spring-boot-starter
    corp_docs = os.path.join(BASE_DIR, "corp-spring-boot-starter", "docs")
    if os.path.exists(corp_docs):
        # Move all contents of corp-spring-boot-starter/docs into central docs folder
        for item in os.listdir(corp_docs):
            shutil.move(os.path.join(corp_docs, item), os.path.join(DOCS_DIR, item))
        shutil.rmtree(corp_docs)
        print("✅ Moved corp-spring-boot-starter/docs -> docs/")

def organize_scripts():
    # Simulations
    sims = glob.glob(os.path.join(BASE_DIR, "simulate_*.py"))
    sims.extend(glob.glob(os.path.join(BASE_DIR, "simulate_*.sh")))
    sims.extend(glob.glob(os.path.join(BASE_DIR, "master_*.py")))
    sims.extend(glob.glob(os.path.join(BASE_DIR, "run_all_simulations.sh")))
    sims.extend(glob.glob(os.path.join(BASE_DIR, "run_pro_performance_and_cost_simulations.py")))
    
    for s in set(sims):
        move_if_exists(os.path.basename(s), SCRIPTS_SIM)
        
    # Scaffolding
    scaffs = glob.glob(os.path.join(BASE_DIR, "scaffold_*.py"))
    scaffs.extend(glob.glob(os.path.join(BASE_DIR, "scaffold_*.sh")))
    scaffs.extend(glob.glob(os.path.join(BASE_DIR, "fix_*.py")))
    scaffs.extend(glob.glob(os.path.join(BASE_DIR, "inject_*.py")))
    scaffs.extend(glob.glob(os.path.join(BASE_DIR, "generate_*.py")))
    scaffs.extend(glob.glob(os.path.join(BASE_DIR, "pack_*.py")))
    
    for s in set(scaffs):
        move_if_exists(os.path.basename(s), SCRIPTS_SCAFFOLD)
        
    # CI/CD
    cicds = glob.glob(os.path.join(BASE_DIR, "final_push*.sh"))
    cicds.extend(glob.glob(os.path.join(BASE_DIR, "git_push_all.sh")))
    cicds.extend(glob.glob(os.path.join(BASE_DIR, "apply_cgroups*.sh")))
    cicds.extend(glob.glob(os.path.join(BASE_DIR, "phase1_git.sh")))
    cicds.extend(glob.glob(os.path.join(BASE_DIR, "update_*.py")))
    
    for s in set(cicds):
        move_if_exists(os.path.basename(s), SCRIPTS_CICD)
        
    # Analysis
    analysis = glob.glob(os.path.join(BASE_DIR, "analyze_cleanliness.py"))
    analysis.extend(glob.glob(os.path.join(BASE_DIR, "run_1M_*.py")))
    
    for s in set(analysis):
        move_if_exists(os.path.basename(s), SCRIPTS_ANALYSIS)
        
    # Other loose scripts
    remaining_py = glob.glob(os.path.join(BASE_DIR, "*.py"))
    remaining_sh = glob.glob(os.path.join(BASE_DIR, "*.sh"))
    for s in remaining_py + remaining_sh:
        move_if_exists(os.path.basename(s), SCRIPTS_DIR)

def fix_script_paths():
    # Since run_all_simulations.sh is now in scripts/simulations/, we need to fix it.
    script_path = os.path.join(SCRIPTS_SIM, "run_all_simulations.sh")
    if os.path.exists(script_path):
        with open(script_path, 'r') as f:
            content = f.read()
        # It just runs python3 simulate_b2g.py etc, which works if executed from SCRIPTS_SIM
        with open(script_path, 'w') as f:
            f.write(content)
            
if __name__ == "__main__":
    print("Iniciando reorganización del ecosistema...")
    makedirs()
    organize_apps()
    organize_core()
    organize_data()
    organize_docs()
    organize_scripts()
    fix_script_paths()
    print("Reorganización finalizada. 🧹")

