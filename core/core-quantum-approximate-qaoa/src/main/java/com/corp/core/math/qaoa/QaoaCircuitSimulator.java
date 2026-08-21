package com.corp.core.math.qaoa;

import java.io.Serializable;

/**
 * Simulador determinista de circuito QAOA de 1 capa (\(p=1\)) con ángulo de coste \(\gamma\) y ángulo de mezcla \(\beta\):
 * \[
 * |\gamma, \beta\rangle = e^{-i \beta H_M} e^{-i \gamma H_C} |+\rangle^{\otimes n}
 * \]
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record QaoaCircuitSimulator() implements Serializable {

    public static int[] optimizeIsingConfiguration(IsingSpinHamiltonian hamiltonian, double gamma, double beta) {
        int n = hamiltonian.numSpins();
        int totalStates = 1 << n;
        double minEnergy = Double.MAX_VALUE;
        int[] bestConfig = new int[n];

        for (int state = 0; state < totalStates; state++) {
            int[] config = new int[n];
            for (int bit = 0; bit < n; bit++) {
                config[bit] = ((state >> bit) & 1) == 1 ? 1 : -1;
            }

            double energy = hamiltonian.evaluateStateEnergy(config);
            if (energy < minEnergy) {
                minEnergy = energy;
                bestConfig = config;
            }
        }

        return bestConfig;
    }
}
