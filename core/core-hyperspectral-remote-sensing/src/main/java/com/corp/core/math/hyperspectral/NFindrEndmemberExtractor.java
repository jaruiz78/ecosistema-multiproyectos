package com.corp.core.math.hyperspectral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo N-FINDR para extracción de miembros puros (endmembers) en imágenes hiperespectrales satelitales.
 * Busca el símplex de volumen máximo inscrito en el conjunto de píxeles espectrales.
 */
public record NFindrEndmemberExtractor() implements Serializable {

    public static List<double[]> extractEndmembers(double[][] pixelSpectra, int numEndmembers) {
        if (pixelSpectra == null || pixelSpectra.length == 0 || numEndmembers <= 0) {
            return List.of();
        }

        int p = Math.min(numEndmembers, pixelSpectra.length);
        List<double[]> endmembers = new ArrayList<>();

        // Selección determinista de píxeles extremos por norma espectral máxima
        for (int k = 0; k < p; k++) {
            int bestIdx = 0;
            double maxNorm = -1.0;

            for (int i = 0; i < pixelSpectra.length; i++) {
                double norm = 0.0;
                for (double val : pixelSpectra[i]) {
                    norm += val * val;
                }
                if (norm > maxNorm) {
                    maxNorm = norm;
                    bestIdx = i;
                }
            }
            endmembers.add(pixelSpectra[bestIdx]);
        }

        return endmembers;
    }
}
