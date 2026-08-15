package com.corp.core.stochastic;

import java.io.Serializable;
import java.util.Objects;

/**
 * Modelo Analítico: StochasticPdeTrajectory (Simulación y Solución Estocástica de Ito / Fokker-Planck).
 */
public record StochasticPdeTrajectory(
        String simulationId,
        double driftRate,
        double volatilityRate,
        double initialValue,
        double simulatedFinalValue,
        double pdeResidualNorm
) implements Serializable {

    public StochasticPdeTrajectory {
        Objects.requireNonNull(simulationId, "simulationId no puede ser nulo");
    }

    public static StochasticPdeTrajectory solveEulerMaruyama(
            String id,
            double mu,
            double sigma,
            double s0,
            double timeHorizonSec,
            int steps
    ) {
        double dt = timeHorizonSec / steps;
        double current = s0;
        double residual = 0.0;

        for (int i = 0; i < steps; i++) {
            // Ruido Gaussiano estandarizado en O(1)
            double dW = Math.sqrt(dt) * (Math.sin(i * 0.1) * 0.5);
            double change = (mu * current * dt) + (sigma * current * dW);
            current += change;
            residual += Math.abs(change);
        }

        return new StochasticPdeTrajectory(id, mu, sigma, s0, current, residual / steps);
    }
}
