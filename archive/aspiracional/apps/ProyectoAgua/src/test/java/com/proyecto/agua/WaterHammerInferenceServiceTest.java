package com.proyecto.agua;

import com.corp.contracts.CellularIrrigationEvent;
import com.proyecto.agua.application.WaterHammerInferenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WaterHammerInferenceService - Golpe de Ariete Joukowsky Off-Heap")
class WaterHammerInferenceServiceTest {

    @Test
    @DisplayName("Estimación Joukowsky de presión hidráulica transitoria ejecuta en O(1)")
    void testJoukowskyPressureEstimation() {
        WaterHammerInferenceService service = new WaterHammerInferenceService();

        CellularIrrigationEvent event = service.evaluatePressureTransient(
                "tenant-segura", "sector-nordeste", "88397064d7fffff", 0.05, 0.20, 2.0
        );

        assertNotNull(event);
        assertEquals("tenant-segura", event.tenantId());
        assertEquals("88397064d7fffff", event.h3Cell());
        assertTrue(event.pressureBar() > 0);
        assertEquals("OPEN", event.valveStatus());
    }
}
