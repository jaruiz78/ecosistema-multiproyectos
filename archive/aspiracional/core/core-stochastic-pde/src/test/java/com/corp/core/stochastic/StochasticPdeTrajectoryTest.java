package com.corp.core.stochastic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StochasticPdeTrajectoryTest {

    @Test
    @DisplayName("Debe resolver trayectoria estocástica y acotar el residual de Fokker-Planck")
    void shouldSolveStochasticTrajectory() {
        StochasticPdeTrajectory trajectory = StochasticPdeTrajectory.solveEulerMaruyama(
                "SPDE-SIM-001",
                0.05,
                0.20,
                100.0,
                1.0,
                100
        );

        assertNotNull(trajectory);
        assertTrue(trajectory.simulatedFinalValue() > 0.0);
        assertTrue(trajectory.pdeResidualNorm() < 5.0);
    }
}
