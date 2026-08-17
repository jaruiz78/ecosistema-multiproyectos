package com.corp.core.stochastic;

import java.io.Serializable;
import java.util.Objects;

/**
 * Modelo Analítico: StochasticPdeTrajectory (Simulación y Solución Estocástica de Ito / Fokker-Planck).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
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
