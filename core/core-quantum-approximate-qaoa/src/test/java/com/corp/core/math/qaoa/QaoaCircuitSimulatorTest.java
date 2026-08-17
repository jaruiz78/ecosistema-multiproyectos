package com.corp.core.math.qaoa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QaoaCircuitSimulatorTest {

    @Test
    @DisplayName("Debe resolver estado fundamental de Ising para grafo simple de 2 espines")
    void testOptimizeIsingConfiguration() {
        // 2 espines acoplados antiferromagnéticamente (J = 1.0)
        double[][] adj = new double[][]{
                {0.0, 1.0},
                {1.0, 0.0}
        };

        var hamiltonian = IsingSpinHamiltonian.maxCutGraph(adj);
        int[] config = QaoaCircuitSimulator.optimizeIsingConfiguration(hamiltonian, 0.5, 0.5);

        assertEquals(2, config.length);
        // La energía mínima se logra cuando config[0] * config[1] = -1 (antiparalelos)
        assertEquals(-1, config[0] * config[1]);
    }
}
