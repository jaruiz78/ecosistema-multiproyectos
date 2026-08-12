#!/usr/bin/env python3
import json

SIMULATION_TEMPLATE = """\
#!/usr/bin/env python3
import json
import time

def run_simulation():
    print("Iniciando simulación para {project_name}...")
    try:
        with open('current_state.json', 'r') as f:
            state = json.load(f)
    except Exception:
        state = {{"month": 0, "active_shocks": []}}
    
    print(f"[{project_name}] Procesando mes {{state.get('month', 0)}}...")
    time.sleep(1)
    
    print(f"✅ Simulación de {project_name} completada exitosamente.")

if __name__ == "__main__":
    run_simulation()
"""

projects = ["Defensa", "VPP", "Circular"]

for p in projects:
    filename = f"/home/jaruiz/Desarrollo/simulate_{p.lower()}.py"
    with open(filename, 'w') as f:
        f.write(SIMULATION_TEMPLATE.format(project_name=f"Proyecto{p}"))
    print(f"Generado {filename}")

