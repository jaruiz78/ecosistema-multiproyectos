import logging
import numpy as np

logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s"
)


class NashEquilibriumSolver:
    """
    Consilium Romano - Pilar VII: Tokenomics y Economía Institucional.
    Audita contratos RWA o PCT buscando "Riesgo Moral" y "Espirales de la Muerte".
    Si el equilibrio de Nash favorece a actores maliciosos (ataque Sybil/Drenaje), se bloquea.
    """

    def __init__(self):
        pass

    def check_for_death_spiral(self, payoff_matrix):
        """
        Payoff matrix: 2x2. Rows: Actor A (Honesto, Malicioso). Cols: Actor B (Honesto, Malicioso).
        """
        logging.info("⚖️ Iniciando solver de Equilibrio de Nash para Tokenomics...")
        # Simplificación: Buscar si la estrategia dominante para ambos es "Maliciosa" (Traición)
        # y si el pago del sistema (sumatorio) en ese equilibrio es negativo (drenaje).

        # Estrategia 0: Honesto. Estrategia 1: Malicioso.
        # payoff_matrix[A][B] = (Pago A, Pago B)

        # Encontramos la mejor respuesta de A para cada B
        best_A_vs_honesto_B = max([0, 1], key=lambda a: payoff_matrix[a][0][0])
        best_A_vs_malicioso_B = max([0, 1], key=lambda a: payoff_matrix[a][1][0])

        best_B_vs_honesto_A = max([0, 1], key=lambda b: payoff_matrix[0][b][1])
        best_B_vs_malicioso_A = max([0, 1], key=lambda b: payoff_matrix[1][b][1])

        # Un equilibrio de Nash en estrategias puras ocurre donde las mejores respuestas coinciden
        nash_equilibria = []
        if best_A_vs_honesto_B == 0 and best_B_vs_honesto_A == 0:
            nash_equilibria.append((0, 0))
        if best_A_vs_malicioso_B == 1 and best_B_vs_honesto_A == 1:
            nash_equilibria.append((1, 0))
        if best_A_vs_honesto_B == 0 and best_B_vs_malicioso_A == 1:
            nash_equilibria.append((0, 1))
        if best_A_vs_malicioso_B == 1 and best_B_vs_malicioso_A == 1:
            nash_equilibria.append((1, 1))

        logging.info(f"Equilibrios de Nash detectados: {nash_equilibria}")

        if (1, 1) in nash_equilibria:
            # Ambos actores tienen incentivos para ser maliciosos
            system_drain = payoff_matrix[1][1][0] + payoff_matrix[1][1][1]
            if system_drain < 0:
                logging.error(
                    "🚨 RIESGO MORAL DETECTADO: El Equilibrio de Nash dominante favorece el comportamiento malicioso y drena el sistema (Espiral de la Muerte)."
                )
                return False

        logging.info(
            "✅ Diseño de Mecanismo válido. No se detectan incentivos perversos críticos."
        )
        return True


def test_token_contract():
    logging.info("Testeando despliegue de contrato Token RWA / PCT Laboral...")
    solver = NashEquilibriumSolver()

    # Matriz tipo "Dilema del Prisionero" donde la traición drena liquidez
    # (Honesto, Malicioso)
    # Pago (A, B)
    payoff = [
        [(5, 5), (-10, 10)],  # A es Honesto
        [(10, -10), (-5, -5)],  # A es Malicioso
    ]

    is_valid = solver.check_for_death_spiral(payoff)
    if not is_valid:
        logging.warning(
            "🚫 Bloqueando despliegue del Smart Contract. Rediseñe la emisión de tokens/puntos."
        )


if __name__ == "__main__":
    test_token_contract()
