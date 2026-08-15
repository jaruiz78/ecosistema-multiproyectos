package com.corp.ecosystem.uspace;

import com.corp.ecosystem.uspace.application.DroneAirspaceService;
import com.corp.ecosystem.uspace.domain.USpaceFlightCorridor;
import com.corp.ecosystem.uspace.domain.port.DroneFlightRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoDroneAirspaceUSpace.
 */
class USpaceFlightCorridorTest {

    static class InMemoryDroneFlightRepository implements DroneFlightRepositoryPort {
        private final Map<USpaceFlightCorridor.FlightPlanId, USpaceFlightCorridor> storage = new ConcurrentHashMap<>();

        @Override
        public USpaceFlightCorridor save(USpaceFlightCorridor corridor) {
            storage.put(corridor.id(), corridor);
            return corridor;
        }

        @Override
        public Optional<USpaceFlightCorridor> findById(USpaceFlightCorridor.FlightPlanId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryDroneFlightRepository repository = new InMemoryDroneFlightRepository();
    private final DroneAirspaceService service = new DroneAirspaceService(repository);

    @Test
    @DisplayName("Debe autorizar plan de vuelo U-Space sin conflictos espaciales")
    void shouldAuthorizeClearUSpaceFlightPlan() {
        List<USpaceFlightCorridor.AirspaceWaypoint3D> waypoints = List.of(
                new USpaceFlightCorridor.AirspaceWaypoint3D(0x89390cb307fffffL, 75.0, System.currentTimeMillis()),
                new USpaceFlightCorridor.AirspaceWaypoint3D(0x89390cb301fffffL, 80.0, System.currentTimeMillis() + 180000)
        );

        USpaceFlightCorridor corridor = service.submitFlightPlan(
                "enaire-uspace-spain",
                "DRONE-MED-007",
                waypoints,
                USpaceFlightCorridor.AltitudeLayer.URBAN_DELIVERY_50_120M,
                false
        );

        assertNotNull(corridor.id());
        assertEquals(USpaceFlightCorridor.FlightDeconflictionStatus.STRATEGIC_DECONFLICTED_CLEAR, corridor.deconflictionStatus());
        assertEquals(2, corridor.flightPath().size());
    }

    @Test
    @DisplayName("Debe marcar conflicto de trayectoria y ordenar re-enrutamiento automático")
    void shouldDetectTrajectoryConflictAndTriggerRerouting() {
        List<USpaceFlightCorridor.AirspaceWaypoint3D> waypoints = List.of(
                new USpaceFlightCorridor.AirspaceWaypoint3D(0x89390cb307fffffL, 150.0, System.currentTimeMillis())
        );

        USpaceFlightCorridor corridor = service.submitFlightPlan(
                "enaire-uspace-spain",
                "EVTOL-AIRTAXI-01",
                waypoints,
                USpaceFlightCorridor.AltitudeLayer.EVTOL_CORRIDOR_120_300M,
                true // Conflicto detectado
        );

        assertEquals(USpaceFlightCorridor.FlightDeconflictionStatus.CONFLICT_DETECTED_REROUTING, corridor.deconflictionStatus());
    }
}
