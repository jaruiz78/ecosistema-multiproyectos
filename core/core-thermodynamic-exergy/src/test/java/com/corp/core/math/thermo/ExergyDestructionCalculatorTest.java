package com.corp.core.math.thermo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExergyDestructionCalculatorTest {

    @Test
    @DisplayName("Debe calcular destrucción de exergía no negativa según Segunda Ley")
    void testComputeExergyDestruction() {
        double exDest = ExergyDestructionCalculator.computeExergyDestructionKw(
                293.15, // 20 C ambiente
                10.0,   // 10 kg/s
                1.2,    // s_in
                1.4,    // s_out
                50.0,   // Q = 50 kW
                350.0   // T_b = 350 K
        );

        assertTrue(exDest >= 0.0);
    }

    @Test
    @DisplayName("Debe calcular eficiencia según la Segunda Ley en rango [0, 1]")
    void testSecondLawEfficiency() {
        double eff = CarnotSecondLawEfficiency.evaluateSecondLawEfficiency(80.0, 100.0);
        assertEquals(0.8, eff, 1e-3);
    }
}
