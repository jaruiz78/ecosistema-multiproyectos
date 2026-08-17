package com.corp.core.math.lbm;

import com.corp.corelbm.domain.LbmFluidVelocityField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LatticeBoltzmannD2Q9EngineTest {

    @Test
    @DisplayName("Debe calcular equilibrio y variables macroscópicas conservando masa")
    void testLbmEquilibriumAndMacroscopic() {
        double rho = 1.0;
        double ux = 0.05;
        double uy = 0.02;

        double[] f = new double[9];
        for (int i = 0; i < 9; i++) {
            f[i] = LatticeBoltzmannD2Q9Engine.computeEquilibrium(i, rho, ux, uy);
            assertTrue(f[i] >= 0.0);
        }

        LbmFluidVelocityField field = LatticeBoltzmannD2Q9Engine.computeMacroscopic(10, 20, f);
        assertNotNull(field);
        assertEquals(1.0, field.densityRho(), 1e-4);
        assertEquals(0.05, field.velocityUx(), 1e-4);
        assertEquals(0.02, field.velocityUy(), 1e-4);
    }
}
