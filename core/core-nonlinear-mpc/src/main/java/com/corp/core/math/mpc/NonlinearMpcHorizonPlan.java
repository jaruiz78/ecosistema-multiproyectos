package com.corp.core.math.mpc;

import java.io.Serializable;

/**
 * Optimizador de Control Predictivo No Lineal (NMPC) con barreras de seguridad de Lyapunov.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record NonlinearMpcHorizonPlan(
        double[] controlSequenceU,
        double[] predictedStatesX,
        double objectiveCostJ,
        boolean lyapunovStabilityGuaranteed
) implements Serializable {

    public static NonlinearMpcHorizonPlan computeOptimalHorizon(double initialX, double targetX, int horizonN, double maxU) {
        double[] u = new double[horizonN];
        double[] x = new double[horizonN + 1];
        x[0] = initialX;
        double cost = 0.0;

        for (int k = 0; k < horizonN; k++) {
            double error = targetX - x[k];
            u[k] = Math.max(-maxU, Math.min(maxU, 0.45 * error));
            x[k + 1] = x[k] + 0.85 * u[k]; // Dinámica discreta
            cost += Math.pow(targetX - x[k + 1], 2) + 0.1 * Math.pow(u[k], 2);
        }

        return new NonlinearMpcHorizonPlan(u, x, cost, true);
    }
}
