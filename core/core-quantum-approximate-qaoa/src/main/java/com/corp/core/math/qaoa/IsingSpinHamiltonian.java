package com.corp.core.math.qaoa;

import java.io.Serializable;

/**
 * Hamiltoniano de espín de Ising para problemas combinatorios (Max-Cut / partición de grafos):
 * \[
 * H_C = \sum_{(i,j) \in E} J_{ij} Z_i Z_j + \sum_{i \in V} h_i Z_i
 * \]
 */
public record IsingSpinHamiltonian(
        int numSpins,
        double[][] couplingMatrixJ,
        double[] localFieldsH
) implements Serializable {

    public static IsingSpinHamiltonian maxCutGraph(double[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;
        double[][] j = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                j[i][k] = 0.5 * adjacencyMatrix[i][k];
            }
        }
        return new IsingSpinHamiltonian(n, j, new double[n]);
    }

    public double evaluateStateEnergy(int[] spinConfiguration) {
        // spinConfiguration \in {-1, +1}^n
        double energy = 0.0;
        for (int i = 0; i < numSpins; i++) {
            for (int j = i + 1; j < numSpins; j++) {
                energy += couplingMatrixJ[i][j] * spinConfiguration[i] * spinConfiguration[j];
            }
            energy += localFieldsH[i] * spinConfiguration[i];
        }
        return energy;
    }
}
