package com.corp.proyectoenergia.application;

import com.corp.proyectoenergia.domain.model.GridSubstationNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link ACOptimalPowerFlowSolver}.
 */
class ACOptimalPowerFlowSolverTest {

    private final ACOptimalPowerFlowSolver solver = new ACOptimalPowerFlowSolver();

    @Test
    @DisplayName("Debe resolver el flujo de potencias AC y verificar límites térmicos")
    void shouldSolvePowerFlowCorrectly() {
        GridSubstationNode sub1 = new GridSubstationNode("SUB_400KV_01", "RED_ELECTRICA", 500000.0, 300000.0, Instant.now());
        GridSubstationNode sub2 = new GridSubstationNode("SUB_220KV_02", "IBERDROLA", 250000.0, 150000.0, Instant.now());

        ACOptimalPowerFlowSolver.TransmissionLine line1 = new ACOptimalPowerFlowSolver.TransmissionLine(
                "LINE_400_220", "SUB_400KV_01", "SUB_220KV_02", 0.02, -0.15, 800.0
        );

        ACOptimalPowerFlowSolver.PowerFlowSolution solution = solver.solvePowerFlow(
                List.of(sub1, sub2), List.of(line1), 500.0
        );

        assertNotNull(solution);
        assertEquals(500.0, solution.totalActiveGenerationMw(), 0.001);
        assertTrue(solution.totalTransmissionLossesMw() > 0.0);
        assertTrue(solution.averageVoltagePu() > 0.95 && solution.averageVoltagePu() < 1.05);
        assertTrue(solution.isWithinThermalLimits());
    }
}
