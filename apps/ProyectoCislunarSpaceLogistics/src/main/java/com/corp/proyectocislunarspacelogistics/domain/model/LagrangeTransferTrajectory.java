package com.corp.proyectocislunarspacelogistics.domain.model;

import java.io.Serializable;

/**
 * Trayectoria orbital en el problema circular restringido de 3 cuerpos (CR3BP)
 * entre la órbita baja terrestre (LEO) y los puntos de Lagrange Tierra-Luna (L1/L2).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record LagrangeTransferTrajectory(
        String missionId,
        String destinationLagrangePoint, // L1, L2, L4, L5
        double deltaVKmPerS,
        double timeOfFlightDays,
        double jacobiConstantC,
        TrajectoryFeasibility feasibility
) implements Serializable {

    public enum TrajectoryFeasibility {
        OPTIMAL_LOW_ENERGY_TRANSIT,
        HIGH_DELTA_V_FAST_TRANSIT,
        FORBIDDEN_HILL_REGION
    }

    public static LagrangeTransferTrajectory create(String id, String point) {
        // En CR3BP, transferencias cuasi-balísticas de baja energía hacia L1/L2 requieren \Delta V \approx 3.8 km/s
        boolean isL1L2 = "L1".equalsIgnoreCase(point) || "L2".equalsIgnoreCase(point);
        double dv = isL1L2 ? 3.85 : 4.50;
        double days = isL1L2 ? 4.8 : 3.2;
        double c = 3.18; // Constante de Jacobi permitiendo cuellos de botella de Hill
        return new LagrangeTransferTrajectory(id, point, dv, days, c, TrajectoryFeasibility.OPTIMAL_LOW_ENERGY_TRANSIT);
    }
}
