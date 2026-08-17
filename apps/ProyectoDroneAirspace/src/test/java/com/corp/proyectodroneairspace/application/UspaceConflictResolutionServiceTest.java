package com.corp.proyectodroneairspace.application;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas TDD Zero-Mockito para {@link UspaceConflictResolutionService}.
 */
class UspaceConflictResolutionServiceTest {

    @Test
    @DisplayName("Debe detectar pérdida de separación y emitir recomendación evasiva")
    void shouldDetectConflictAndIssueAdvisory() {
        UspaceConflictResolutionService service = new UspaceConflictResolutionService();

        DroneFlightRoute droneA = new DroneFlightRoute(UUID.randomUUID().toString(), "TENANT_MADRID", "DRONE_A", 100.0, "ACTIVE", java.time.Instant.now());
        DroneFlightRoute droneB = new DroneFlightRoute(UUID.randomUUID().toString(), "TENANT_MADRID", "DRONE_B", 100.0, "ACTIVE", java.time.Instant.now());

        // Conflicto: separación horizontal de 30m (<50m) y separación vertical de 10m (<20m)
        var result = service.assessConflict(droneA, 120.0, droneB, 110.0, 30.0);

        assertTrue(result.conflictDetected());
        assertEquals("DRONE_A_CLIMB_10M_DRONE_B_DESCEND_10M", result.advisoryAction());
    }

    @Test
    @DisplayName("Debe confirmar trayectoria libre cuando la separación es suficiente")
    void shouldConfirmClearFlightPathWhenSeparated() {
        UspaceConflictResolutionService service = new UspaceConflictResolutionService();

        DroneFlightRoute droneA = new DroneFlightRoute(UUID.randomUUID().toString(), "TENANT_BCN", "DRONE_A", 50.0, "ACTIVE", java.time.Instant.now());
        DroneFlightRoute droneB = new DroneFlightRoute(UUID.randomUUID().toString(), "TENANT_BCN", "DRONE_B", 50.0, "ACTIVE", java.time.Instant.now());

        var result = service.assessConflict(droneA, 150.0, droneB, 100.0, 100.0);

        assertFalse(result.conflictDetected());
        assertEquals("CLEAR_FLIGHT_PATH", result.advisoryAction());
    }
}
