package com.corp.corelbm.application;

import com.corp.corelbm.domain.LbmFluidVelocityField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LbmIntegrationTest {

    @Test
    @DisplayName("Debe simular un nodo de fluido mediante caso de uso LBM")
    void testSimulateFluidNode() {
        var useCase = new LatticeBoltzmannFluidUseCase();
        LbmFluidVelocityField result = useCase.simulateFluidNode(5, 5, 1.2, 0.08, 0.01, 0.6);

        assertNotNull(result);
        assertEquals(5, result.gridX());
        assertEquals(5, result.gridY());
        assertEquals(1.2, result.densityRho(), 1e-4);
        assertTrue(result.vorticityOmega() > 0.0);
    }
}
