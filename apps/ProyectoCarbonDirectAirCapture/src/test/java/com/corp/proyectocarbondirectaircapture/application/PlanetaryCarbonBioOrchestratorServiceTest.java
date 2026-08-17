package com.corp.proyectocarbondirectaircapture.application;

import com.corp.proyectocarbondirectaircapture.application.service.PlanetaryCarbonBioOrchestratorService;
import com.corp.proyectocarbondirectaircapture.domain.model.DirectAirCaptureFacility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanetaryCarbonBioOrchestratorServiceTest {

    @Test
    @DisplayName("Debe ejecutar ciclo sinérgico planetario: teledetección hiperespectral, mineralización DAC y biobanco eDNA")
    void testPlanetaryCarbonBioSynergy() {
        var orchestrator = new PlanetaryCarbonBioOrchestratorService();

        DirectAirCaptureFacility dac = DirectAirCaptureFacility.create("DAC-ICELAND-HELLISHEIDI", 500.0);
        long h3Cell = 0x881f1d4887fffffL;

        double[] pixel = new double[]{0.8, 0.2, 0.1};
        double[][] library = new double[][]{
                {0.9, 0.1, 0.05},
                {0.1, 0.85, 0.3}
        };

        Map<String, Integer> eDnaReads = Map.of(
                "Betula_nana", 45,
                "Salix_herbacea", 30,
                "Stereocaulon_vesuvianum", 60
        );

        var result = orchestrator.executeCarbonMineralizationAndBioMonitoring(
                dac, h3Cell, 100.0, pixel, library, eDnaReads
        );

        assertNotNull(result);
        assertEquals("DAC-ICELAND-HELLISHEIDI", result.facilityId());
        assertEquals(95.0, result.mineralizedCo2Tonnes(), 1e-3);
        assertTrue(result.shannonDiversityIndexH() > 0.8);
        assertTrue(result.carbonNegativeVerified());
    }
}
