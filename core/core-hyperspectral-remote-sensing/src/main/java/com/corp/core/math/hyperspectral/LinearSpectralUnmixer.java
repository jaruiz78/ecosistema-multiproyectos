package com.corp.core.math.hyperspectral;

import java.io.Serializable;

/**
 * Desmezclador lineal de firmas espectrales (Linear Spectral Mixture Model - LSMM):
 * \[
 * y = \sum_{i=1}^M \alpha_i e_i + n, \quad \text{con } \alpha_i \ge 0, \sum \alpha_i = 1
 * \]
 */
public record LinearSpectralUnmixer() implements Serializable {

    public static double[] estimateAbundances(double[] mixedPixel, double[][] endmembers) {
        int m = endmembers.length;
        double[] abundances = new double[m];
        double sum = 0.0;

        for (int i = 0; i < m; i++) {
            // Proyección euclídea simplificada
            double dot = 0.0;
            double normE = 0.0;
            for (int b = 0; b < Math.min(mixedPixel.length, endmembers[i].length); b++) {
                dot += mixedPixel[b] * endmembers[i][b];
                normE += endmembers[i][b] * endmembers[i][b];
            }
            abundances[i] = Math.max(0.0, dot / Math.max(1e-9, normE));
            sum += abundances[i];
        }

        // Normalizar para que la suma de abundancias fraccionarias sea 1.0
        if (sum > 0.0) {
            for (int i = 0; i < m; i++) {
                abundances[i] /= sum;
            }
        }

        return abundances;
    }
}
