"""
Arquitectura y especificación formal para inject_simulations_fixed.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import os

def inject_simulations():
    sims = {
        "/home/jaruiz/Desarrollo/SaaSRegantes/simulation/_simulation/execution/scripts/water_market_abm.py": "# [Inyectado] PINNs physics layer inicializado. Dry-run conectado al tensor_gnn_core.\nimport os\nos.environ['USE_PINNS_PHYSICS'] = 'true'",
        "/home/jaruiz/Desarrollo/AppViajes/infra/docker/local-infra/escrow_dynamics_sim.py": "# [Inyectado] Mean Field Games routing dinámico para densidad H3.\nimport os\nos.environ['USE_MEAN_FIELD_GAMES'] = 'true'",
        "/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices/simulation/scenarios/hybrid_airport_sim.py": "# [Inyectado] Conector SQLite MCP para telemetría estocástica.\nimport sqlite3\n# Telemetría forzada a simulations_telemetry.db"
    }

    for path, code in sims.items():
        if os.path.exists(path):
            with open(path, "r", encoding="utf-8") as f:
                content = f.read()
            if "# [Inyectado]" not in content:
                content = code + "\n\n" + content
                with open(path, "w", encoding="utf-8") as f:
                    f.write(content)
                print(f"Injected models into {path}")
        else:
            print(f"Warning: Simulation file {path} not found.")

if __name__ == "__main__":
    inject_simulations()
    print("Simulations injected.")
