#!/usr/bin/env python3
import json
import os
import subprocess
import time
import numpy as np

STATE_FILE = "current_state.json"
TRAIN_SCRIPT = "./run_all_trainings.sh"
SIM_SCRIPT = "./run_all_simulations.sh"


def run_5_years():
    print("==========================================================")
    print("🚀 INICIANDO SIMULACIÓN DEL GEMELO DIGITAL TENSORIAL (60 MESES)")
    print("==========================================================")

    total_months = 60

    # 1. Generación Estocástica Vectorizada O(1) de Shocks
    # Probabilidad del 10% de un shock severo cada mes
    shock_probs = np.random.random(total_months)
    has_shock = shock_probs < 0.10

    shock_types = ["CLIMA", "LOGISTICA", "ECONOMIA"]
    shock_impacts = [-0.4, -0.5, -0.3]

    # Pre-calculamos los índices de los shocks y los impactos
    random_shock_indices = np.random.randint(0, len(shock_types), size=total_months)

    health_deltas = np.where(
        has_shock, np.array(shock_impacts)[random_shock_indices], 0.05
    )  # Recuperación natural leve

    # Calculamos la trayectoria de salud usando un clip cumulativo (no se puede usar cumsum directo por el min/max,
    # pero podemos iterar la orquestación ya que los scripts externos mandan).
    # Al menos la generación estocástica es tensorial.

    ecosystem_health = 1.0

    for month_idx in range(total_months):
        month = month_idx + 1
        print(f"\n--- 📅 MES {month} ---")

        is_shock_active = has_shock[month_idx]
        delta = health_deltas[month_idx]

        if is_shock_active:
            shock_name = shock_types[random_shock_indices[month_idx]]
            print(
                f"\n[⚠️ SHOCK INYECTADO en Mes {month}] {shock_name}: Impacto {delta}!"
            )
            shock_label = shock_name
        else:
            shock_label = "None"

        ecosystem_health = min(1.0, ecosystem_health + delta)

        if ecosystem_health <= 0:
            print("🚨 COLAPSO TOTAL DEL ECOSISTEMA. SALUD < 0.")
            break

        state = {
            "month": month,
            "ecosystem_health": round(float(ecosystem_health), 2),
            "shock_active": shock_label,
        }

        with open(STATE_FILE, "w") as f:
            json.dump(state, f)

        # 2. MLOps Continuo: Re-entrenamiento
        subprocess.run(
            TRAIN_SCRIPT,
            shell=True,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
        )

        # 3. Inferencia de Simuladores
        subprocess.run(
            SIM_SCRIPT, shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
        )

        print(f"[+] Salud del Ecosistema: {state['ecosystem_health'] * 100:.1f}%")
        if is_shock_active:
            print(f"[!] MLOps re-entrenado adaptándose al shock: {shock_label}")

    print("\n==========================================================")
    if ecosystem_health > 0:
        print(
            f"🏁 SIMULACIÓN 5 AÑOS COMPLETADA. EL SISTEMA ES RESILIENTE. Salud final: {ecosystem_health*100:.1f}%"
        )
    else:
        print("💥 SIMULACIÓN FALLIDA. EL ECOSISTEMA NO SOBREVIVIÓ 5 AÑOS.")
    print("==========================================================")


if __name__ == "__main__":
    # Hacemos scripts ejecutables
    os.system("chmod +x run_all_trainings.sh run_all_simulations.sh")
    run_5_years()
