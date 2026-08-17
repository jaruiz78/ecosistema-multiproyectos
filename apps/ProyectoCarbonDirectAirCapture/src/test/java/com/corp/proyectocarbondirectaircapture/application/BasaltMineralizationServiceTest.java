package com.corp.proyectocarbondirectaircapture.application;

import com.corp.proyectocarbondirectaircapture.application.service.BasaltMineralizationService;
import com.corp.proyectocarbondirectaircapture.infrastructure.adapter.out.persistence.InMemoryDacFacilityRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasaltMineralizationServiceTest {

    @Test
    @DisplayName("Debe inyectar lote de CO2 y persistir instalación actualizada")
    void testInjectCo2ToBasalt() {
        var repo = new InMemoryDacFacilityRepositoryAdapter();
        var service = new BasaltMineralizationService(repo);

        var result = service.injectCo2ToBasalt("DAC-01", 100.0);

        assertNotNull(result);
        assertEquals("DAC-01", result.facilityId());
        assertEquals(95.0, result.cumulativeMineralizedTonnesCo2(), 1e-3);
    }
}
