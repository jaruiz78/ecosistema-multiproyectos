import time
import random
import logging

logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s"
)


class EROEIAuditor:
    """
    Consilium Romano - Pilar V: Física y Termodinámica.
    Calcula el Energy Return on Energy Invested (EROEI).
    Si EROEI < 1.0, el modelo predictivo consume más energía de la que ahorra.
    """

    def __init__(self):
        self.cpu_joules_per_ms = 0.05  # Estimación de coste térmico GCP CPU por ms

    def audit_simulation(self, cpu_time_ms, expected_savings_joules):
        invested_energy_joules = cpu_time_ms * self.cpu_joules_per_ms
        eroei = (
            expected_savings_joules / invested_energy_joules
            if invested_energy_joules > 0
            else float("inf")
        )

        logging.info(
            f"Auditoría EROEI - Inversión Computacional: {invested_energy_joules:.2f} J | Ahorro Esperado: {expected_savings_joules:.2f} J"
        )
        logging.info(f"Ratio EROEI calculado: {eroei:.2f}")

        if eroei < 1.0:
            logging.error(
                "🚨 CONFLICTO TERMODINÁMICO: EROEI < 1.0. La simulación gasta más energía de la que ahorra."
            )
            return False
        return True


def run_pypsa_mock_simulation():
    logging.info("Iniciando OPF (Optimal Power Flow) en ProyectoEnergia...")
    start = time.time()
    time.sleep(0.6)  # Simulando 600ms de cálculo pesado
    end = time.time()
    cpu_ms = (end - start) * 1000

    # Supongamos un ahorro en microrred de 20 Joules
    savings = 20.0

    auditor = EROEIAuditor()
    is_valid = auditor.audit_simulation(cpu_ms, savings)

    if not is_valid:
        logging.warning(
            "Fallback activado: Destruyendo instancia de simulación y utilizando heurística local O(1)."
        )
        logging.info("Ejecución O(1) completada en 2ms. Consumo térmico: 0.1 Joules.")
        return False
    return True


if __name__ == "__main__":
    run_pypsa_mock_simulation()
