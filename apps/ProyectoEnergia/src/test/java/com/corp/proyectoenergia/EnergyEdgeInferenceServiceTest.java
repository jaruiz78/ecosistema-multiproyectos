package com.corp.proyectoenergia;

import com.corp.contracts.GeoLocationH3Record;
import com.corp.contracts.TelemetryEnkfEvent;
import com.corp.proyectoenergia.application.EnergyEdgeInferenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EnergyEdgeInferenceService - Inferencia Edge AI Off-Heap")
class EnergyEdgeInferenceServiceTest {

    @Test
    @DisplayName("Estimación de estabilidad de red energética ejecuta en O(1) con tensores directos")
    void testGridStabilityInference() {
        EnergyEdgeInferenceService service = new EnergyEdgeInferenceService();
        GeoLocationH3Record location = GeoLocationH3Record.of("88397064d7fffff", 37.9838, -1.1280, 8);

        TelemetryEnkfEvent event = service.estimateGridStability(location, 100.0, 75.0);

        assertNotNull(event);
        assertEquals("88397064d7fffff", event.h3Cell());
        assertTrue(event.converged());
        assertTrue(event.covarianceTrace() < 0.5);
        assertEquals(4, event.stateVector().length);
    }
}
