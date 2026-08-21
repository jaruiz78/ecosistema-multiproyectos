package com.corp.core.math.game;

import java.io.Serializable;

/**
 * Solucionador analítico de equilibrios líder-seguidor de Stackelberg en $O(1)$.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record StackelbergEquilibriumSolver() implements Serializable {

    public record EquilibriumResult(
            double leaderAction,
            double followerBestResponse,
            double leaderPayoff,
            double followerPayoff
    ) implements Serializable {}

    public static EquilibriumResult solveLinearCournot(double marketDemandA, double marginalCostC) {
        // Demanda P = a - (q_L + q_F), Costes = c
        // Mejor respuesta seguidor: q_F(q_L) = (a - c - q_L) / 2
        // Líder maximiza (a - q_L - q_F - c) * q_L -> q_L* = (a - c) / 2
        // Seguidor q_F* = (a - c) / 4
        double delta = Math.max(0.0, marketDemandA - marginalCostC);
        double qLeader = delta / 2.0;
        double qFollower = delta / 4.0;

        double price = marketDemandA - (qLeader + qFollower);
        double leaderPayoff = (price - marginalCostC) * qLeader;
        double followerPayoff = (price - marginalCostC) * qFollower;

        return new EquilibriumResult(qLeader, qFollower, leaderPayoff, followerPayoff);
    }
}
