package com.corp.core.transport;

import java.io.Serializable;
import java.util.Objects;

/**
 * Modelo Analítico: OptimalTransportPlan (Transporte Óptimo de Monge-Kantorovich & Distancia Wasserstein W1).
 */
public record OptimalTransportPlan(
        String planId,
        double[] sourceDistribution,
        double[] targetDistribution,
        double wassersteinDistanceW1,
        boolean isBalanced
) implements Serializable {

    public OptimalTransportPlan {
        Objects.requireNonNull(planId, "planId no puede ser nulo");
        Objects.requireNonNull(sourceDistribution, "sourceDistribution no puede ser nulo");
        Objects.requireNonNull(targetDistribution, "targetDistribution no puede ser nulo");
    }

    public static OptimalTransportPlan computeW1(String id, double[] source, double[] target) {
        if (source.length != target.length) {
            throw new IllegalArgumentException("Las distribuciones origen y destino deben tener la misma dimensión");
        }

        double w1 = 0.0;
        double cumulativeDiff = 0.0;

        // Cálculo analítico de la métrica de Wasserstein 1D en O(N)
        for (int i = 0; i < source.length; i++) {
            cumulativeDiff += (source[i] - target[i]);
            w1 += Math.abs(cumulativeDiff);
        }

        boolean balanced = Math.abs(cumulativeDiff) < 1e-4;
        return new OptimalTransportPlan(id, source, target, w1, balanced);
    }
}
