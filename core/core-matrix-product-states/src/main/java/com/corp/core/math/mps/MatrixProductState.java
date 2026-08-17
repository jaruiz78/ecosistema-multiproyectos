package com.corp.core.math.mps;

import java.io.Serializable;
import java.util.List;

/**
 * Representación de un estado cuántico o tensor de alta dimensión en forma de Matrix Product State (MPS):
 * \[
 * |\psi\rangle = \sum_{s_1, \dots, s_N} A^{[1] s_1} A^{[2] s_2} \cdots A^{[N] s_N} |s_1 s_2 \dots s_N\rangle
 * \]
 * Reduce la dimensionalidad exponencial \(d^N\) a polinomial \(O(N \cdot d \cdot \chi^2)\), donde \(\chi\) es la dimensión de enlace (bond dimension).
 */
public record MatrixProductState(
        int numSites,
        int physicalDimension, // d (ej. 2 para qubits/espines)
        int bondDimensionChi,  // \chi (dimensión de entrelazamiento máximo)
        List<double[][][]> siteTensors // A^{[i]} con dimensiones [chi_left][chi_right][d]
) implements Serializable {

    public double calculateNorm() {
        // Cálculo de norma por contracción secuencial en O(N \cdot d \cdot \chi^3)
        if (siteTensors == null || siteTensors.isEmpty()) {
            return 0.0;
        }
        double normAccumulator = 1.0;
        for (double[][][] tensor : siteTensors) {
            double siteSum = 0.0;
            for (double[][] matrix : tensor) {
                for (double[] row : matrix) {
                    for (double val : row) {
                        siteSum += val * val;
                    }
                }
            }
            normAccumulator *= Math.max(1e-12, siteSum);
        }
        return Math.sqrt(normAccumulator);
    }
}
