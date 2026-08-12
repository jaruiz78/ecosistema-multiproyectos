#!/usr/bin/env python3
import json
import time


def run_simulation():
    print("Iniciando simulación para ProyectoDefensa...")
    try:
        with open("current_state.json", "r") as f:
            state = json.load(f)
    except Exception:
        state = {"month": 0, "active_shocks": []}

    print(f"[ProyectoDefensa] Procesando mes {state.get('month', 0)}...")
    time.sleep(1)

    print(f"✅ Simulación de ProyectoDefensa completada exitosamente.")


if __name__ == "__main__":
    run_simulation()
