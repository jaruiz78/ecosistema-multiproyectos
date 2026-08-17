package com.corp.proyectoquantummaterialsgraphene.application;

import com.corp.proyectoquantummaterialsgraphene.application.service.GrapheneSuperconductivityService;
import com.corp.proyectoquantummaterialsgraphene.infrastructure.adapter.out.persistence.InMemoryGrapheneRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrapheneSuperconductivityServiceTest {

    @Test
    @DisplayName("Debe analizar muestra de heteroestructura y persistir resultado")
    void testAnalyzeTwistAngleSample() {
        var repo = new InMemoryGrapheneRepositoryAdapter();
        var service = new GrapheneSuperconductivityService(repo);

        var result = service.analyzeTwistAngleSample("SAMPLE-TWIST-01", 1.09);

        assertNotNull(result);
        assertEquals("SAMPLE-TWIST-01", result.sampleId());
        assertEquals(1.7, result.criticalTemperatureKelvin(), 1e-3);
    }
}
