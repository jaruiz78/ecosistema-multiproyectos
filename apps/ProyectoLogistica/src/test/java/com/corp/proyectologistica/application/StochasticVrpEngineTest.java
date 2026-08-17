package com.corp.proyectologistica.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link StochasticVrpEngine}.
 */
class StochasticVrpEngineTest {

    private final StochasticVrpEngine engine = new StochasticVrpEngine();

    @Test
    @DisplayName("Debe calcular una ruta óptima respetando ventanas de tiempo y calculando emisiones")
    void shouldComputeOptimalTourCorrectly() {
        StochasticVrpEngine.DeliveryStop stop1 = new StochasticVrpEngine.DeliveryStop(
                "STOP_01", "88390cb337fffff", 40.4168, -3.7038, 10, 10, 60
        );
        StochasticVrpEngine.DeliveryStop stop2 = new StochasticVrpEngine.DeliveryStop(
                "STOP_02", "88390cb335fffff", 40.4268, -3.7038, 15, 60, 120
        );

        StochasticVrpEngine.OptimizedTour tour = engine.computeOptimalTour(
                "VAN_ECO_01", 40.4000, -3.7000, List.of(stop2, stop1)
        );

        assertNotNull(tour);
        assertEquals("VAN_ECO_01", tour.vehicleId());
        assertEquals(2, tour.orderedStopIds().size());
        // Debe ordenar primero STOP_01 por ventana de tiempo anterior
        assertEquals("STOP_01", tour.orderedStopIds().get(0));
        assertEquals("STOP_02", tour.orderedStopIds().get(1));
        assertEquals(25, tour.totalDeliveredPackages());
        assertTrue(tour.totalDistanceKm() > 0.0);
        assertTrue(tour.totalCarbonEmissionKg() > 0.0);
    }
}
