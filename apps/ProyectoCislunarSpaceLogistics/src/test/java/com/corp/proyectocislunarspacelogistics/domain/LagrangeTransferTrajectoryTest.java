package com.corp.proyectocislunarspacelogistics.domain;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTransferTrajectory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LagrangeTransferTrajectoryTest {

    @Test
    @DisplayName("Debe calcular transferencia de baja energía hacia punto L2 Tierra-Luna")
    void testLowEnergyTransferL2() {
        LagrangeTransferTrajectory traj = LagrangeTransferTrajectory.create("ARTEMIS-GATEWAY-L2", "L2");

        assertEquals(3.85, traj.deltaVKmPerS(), 1e-3);
        assertEquals(4.8, traj.timeOfFlightDays(), 1e-3);
        assertEquals(LagrangeTransferTrajectory.TrajectoryFeasibility.OPTIMAL_LOW_ENERGY_TRANSIT, traj.feasibility());
    }
}
