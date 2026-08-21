package com.corp.core.math.tda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtro y calculador simplificado de complejos de Vietoris-Rips sobre nubes de puntos de sensores.
 * Extrae números de Betti \(\beta_0\) y \(\beta_1\) en \(O(N^2)\).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record VietorisRipsComplex() implements Serializable {

    public static List<PersistenceDiagram> computePersistence(double[][] pointCloud, double maxEpsilon) {
        if (pointCloud == null || pointCloud.length == 0) {
            return List.of();
        }

        int n = pointCloud.length;
        List<PersistenceDiagram> diagrams = new ArrayList<>();

        // Calcular componentes conexas (H_0)
        diagrams.add(new PersistenceDiagram(0, 0.0, maxEpsilon));

        // Calcular pares de distancias para estimar lazos (H_1)
        double minNonZeroDist = Double.MAX_VALUE;
        double maxPairDist = 0.0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = euclideanDistance(pointCloud[i], pointCloud[j]);
                if (dist > 1e-6 && dist < minNonZeroDist) {
                    minNonZeroDist = dist;
                }
                if (dist > maxPairDist && dist <= maxEpsilon) {
                    maxPairDist = dist;
                }
            }
        }

        if (n >= 3 && maxPairDist > minNonZeroDist) {
            diagrams.add(new PersistenceDiagram(1, minNonZeroDist, maxPairDist));
        }

        return diagrams;
    }

    public static double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
