package com.corp.proyectologistica;

import com.corp.contracts.GeoLocationH3Record;
import com.corp.contracts.TelemetryEnkfEvent;
import com.corp.proyectologistica.application.LogisticsEdgeRoutingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LogisticsEdgeRoutingService - Estimación Logística Off-Heap")
class LogisticsEdgeRoutingServiceTest {

    @Test
    @DisplayName("Cálculo de ventana de entrega vehicular ejecuta con memoria directa off-heap")
    void testDeliveryWindowInference() {
        LogisticsEdgeRoutingService service = new LogisticsEdgeRoutingService();
        GeoLocationH3Record orig = GeoLocationH3Record.of("88397064d7fffff", 37.9838, -1.1280, 8);
        GeoLocationH3Record dest = GeoLocationH3Record.of("88397064d5fffff", 37.9900, -1.1300, 8);

        TelemetryEnkfEvent event = service.estimateDeliveryWindow(orig, dest, 450.0, 1000.0);

        assertNotNull(event);
        assertEquals("88397064d5fffff", event.h3Cell());
        assertTrue(event.converged());
        assertTrue(event.covarianceTrace() < 0.5);
    }
}
