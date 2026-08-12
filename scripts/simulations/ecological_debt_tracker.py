import logging

logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s"
)


class EcologicalDebtTracker:
    """
    Consilium Romano - Pilar VIII: Ecología y Límites Planetarios.
    Aplica el 'Hard Cap' algorítmico al consumo hídrico en SaaSRegantes y
    audita la huella de carbono de las computaciones de IA.
    """

    def __init__(self):
        self.water_hard_cap_liters = 50000.0  # Límite máximo permisible por hectárea

    def audit_water_yield(self, projected_liters, projected_economic_yield):
        logging.info(
            f"🌿 Auditoría Ecológica - Proyección Hídrica: {projected_liters} L | Beneficio: ${projected_economic_yield}"
        )

        if projected_liters > self.water_hard_cap_liters:
            logging.error(
                f"🚨 DEUDA ECOLÓGICA CRÍTICA: Se superó el Hard Cap Hídrico ({projected_liters}L > {self.water_hard_cap_liters}L)."
            )
            logging.warning(
                "💧 Abortando predicción de máxima rentabilidad. Forzando Riego de Supervivencia."
            )
            return False

        logging.info("✅ Cumplimiento de Límites Planetarios confirmado.")
        return True


def test_ecological_debt():
    logging.info("Iniciando orquestación de riego en SaaSRegantes...")
    tracker = EcologicalDebtTracker()

    # Caso 1: Riego eficiente
    tracker.audit_water_yield(30000, 1500)

    # Caso 2: Pico de rentabilidad del aguacate que agota el pozo
    tracker.audit_water_yield(80000, 5000)


if __name__ == "__main__":
    test_ecological_debt()
