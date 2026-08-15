package com.corp.core.math.mpc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonlinearMpcHorizonPlanTest {

    @Test
    @DisplayName("Debe converger al estado objetivo garantizando estabilidad de Lyapunov")
    void shouldConvergeToTargetWithLyapunovStability() {
        NonlinearMpcHorizonPlan plan = NonlinearMpcHorizonPlan.computeOptimalHorizon(10.0, 50.0, 15, 25.0);

        assertNotNull(plan);
        assertTrue(plan.lyapunovStabilityGuaranteed());
        assertEquals(15, plan.controlSequenceU().length);
        assertEquals(16, plan.predictedStatesX().length);
        // El último estado predicho debe estar muy cerca de 50.0
        assertEquals(50.0, plan.predictedStatesX()[15], 1.0);
    }
}
