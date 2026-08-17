package com.corp.corehyperspectral;

import com.corp.corehyperspectral.application.SatelliteHyperspectralAnalysisUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HyperspectralIntegrationTest {

    @Test
    @DisplayName("Debe procesar escena PRISMA y calcular abundancias de minerales críticos")
    void testProcessSentinelPrismaSceneIntegration() {
        SatelliteHyperspectralAnalysisUseCase useCase = new SatelliteHyperspectralAnalysisUseCase();
        double[] pixel = new double[]{0.6, 0.4, 0.2};
        double[][] library = new double[][]{
                {0.9, 0.1, 0.05},
                {0.1, 0.85, 0.3}
        };

        var signature = useCase.processSentinelPrismaScene("PRISMA-SCENE-2026-08", 0x881f1d4887fffffL, pixel, library);

        assertNotNull(signature);
        assertEquals("PRISMA-SCENE-2026-08", signature.sceneId());
        assertTrue(signature.soilCarbonIndex() > 0.0);
    }
}
