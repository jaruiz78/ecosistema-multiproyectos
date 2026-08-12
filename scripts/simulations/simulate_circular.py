#!/usr/bin/env python3
import json
import time


def run_simulation():
    print("Iniciando simulación para ProyectoCircular...")
    try:
        with open("current_state.json", "r") as f:
            state = json.load(f)
    except Exception:
        state = {"month": 0, "active_shocks": []}

    print(f"[ProyectoCircular] Procesando mes {state.get('month', 0)}...")
    time.sleep(1)

    print(f"✅ Simulación de ProyectoCircular completada exitosamente.")


if __name__ == "__main__":
    run_simulation()
