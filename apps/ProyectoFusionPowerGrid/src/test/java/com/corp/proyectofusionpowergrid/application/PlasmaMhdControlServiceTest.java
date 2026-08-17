package com.corp.proyectofusionpowergrid.application;

import com.corp.proyectofusionpowergrid.application.service.PlasmaMhdControlService;
import com.corp.proyectofusionpowergrid.infrastructure.adapter.out.persistence.InMemoryTokamakPlasmaRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlasmaMhdControlServiceTest {

    @Test
    @DisplayName("Debe regular estabilidad de plasma y persistir estado")
    void testRegulatePlasmaStability() {
        var repo = new InMemoryTokamakPlasmaRepositoryAdapter();
        var service = new PlasmaMhdControlService(repo);

        var result = service.regulatePlasmaStability("ITER-DEMO-01", 18.5, 2.1);

        assertNotNull(result);
        assertEquals("ITER-DEMO-01", result.reactorId());
        assertEquals(18.5, result.electronTemperatureKeV(), 1e-3);
    }
}
