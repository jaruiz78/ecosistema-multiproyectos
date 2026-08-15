package com.corp.core.transport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptimalTransportPlanTest {

    @Test
    @DisplayName("Debe calcular distancia de Wasserstein 1D balanceada en O(N)")
    void shouldComputeWassersteinDistance() {
        double[] source = new double[]{0.5, 0.5, 0.0, 0.0};
        double[] target = new double[]{0.0, 0.0, 0.5, 0.5};

        OptimalTransportPlan plan = OptimalTransportPlan.computeW1("OT-PLAN-001", source, target);

        assertNotNull(plan);
        assertTrue(plan.isBalanced());
        assertEquals(2.0, plan.wassersteinDistanceW1(), 1e-4);
    }
}
