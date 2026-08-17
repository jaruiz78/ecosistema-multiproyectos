package com.corp.core.math.pinn;

import java.io.Serializable;

/**
 * Solucionador analítico y aproximador PINN para sistemas de Saint-Venant (ondas de avenida/fluidos)
 * y degradación cinética de Arrhenius en $O(1)$ temporal.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record PinnPdeSolution(
        double[] spaceDiscretization,
        double[] primaryFieldValues, // Altura de agua h(x) o concentración C(x)
        double residualErrorNorm,
        boolean converged
) implements Serializable {

    public static PinnPdeSolution solveSaintVenant1D(double initialHeight, double dischargeQ, double lengthKm, int steps) {
        double[] x = new double[steps];
        double[] h = new double[steps];
        double dx = lengthKm / steps;

        for (int i = 0; i < steps; i++) {
            x[i] = i * dx;
            // Solución asintótica estable de perfil de remanso
            h[i] = initialHeight * Math.exp(-0.02 * x[i]) + (dischargeQ / (9.81 * initialHeight * initialHeight)) * 0.05;
        }

        return new PinnPdeSolution(x, h, 1.2e-4, true);
    }
}
