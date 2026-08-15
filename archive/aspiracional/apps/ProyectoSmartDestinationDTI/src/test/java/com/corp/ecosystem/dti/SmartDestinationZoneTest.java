package com.corp.ecosystem.dti;

import com.corp.ecosystem.dti.application.SmartDestinationService;
import com.corp.ecosystem.dti.domain.SmartDestinationZone;
import com.corp.ecosystem.dti.domain.port.DestinationZoneRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoSmartDestinationDTI.
 */
class SmartDestinationZoneTest {

    static class InMemoryZoneRepository implements DestinationZoneRepositoryPort {
        private final Map<SmartDestinationZone.ZoneId, SmartDestinationZone> storage = new ConcurrentHashMap<>();

        @Override
        public SmartDestinationZone save(SmartDestinationZone zone) {
            storage.put(zone.id(), zone);
            return zone;
        }

        @Override
        public Optional<SmartDestinationZone> findById(SmartDestinationZone.ZoneId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryZoneRepository repository = new InMemoryZoneRepository();
    private final SmartDestinationService service = new SmartDestinationService(repository);

    @Test
    @DisplayName("Debe registrar zona de patrimonio histórico en estado GREEN_OPTIMAL")
    void shouldRegisterHistoricZoneInGreenOptimalState() {
        SmartDestinationZone zone = service.registerDestinationZone(
                "dti-mallorca",
                "Catedral de Palma & Casco Antiguo",
                0x88390cb307fffffL,
                SmartDestinationZone.ZoneType.HISTORIC_CENTER,
                3000, // max 3000 visitantes
                75.0,
                List.of(
                        new SmartDestinationZone.DispersionRoute("ROUTE-1", "Castell de Bellver", 0x88390cb301fffffL, 20, 25)
                )
        );

        assertNotNull(zone.id());
        assertEquals(SmartDestinationZone.ZoneAlertLevel.GREEN_OPTIMAL, zone.alertLevel());
        assertEquals(3000, zone.limits().maxSimultaneousVisitors());
    }

    @Test
    @DisplayName("Debe activar ORANGE_DISPERSION_ACTIVE cuando la ocupación supere el 85%")
    void shouldTriggerOrangeDispersionWhenOccupancyExceedsThreshold() {
        SmartDestinationZone zone = service.registerDestinationZone(
                "dti-ibiza",
                "Playa de Ses Salines",
                0x88390cb307fffffL,
                SmartDestinationZone.ZoneType.BEACH_COASTAL,
                1000,
                80.0,
                List.of()
        );

        // Ingesta de telemetría: 890 personas (89% de capacidad)
        SmartDestinationZone updated = service.ingestCrowdTelemetry(zone.id(), 890, 78.5);

        assertEquals(SmartDestinationZone.ZoneAlertLevel.ORANGE_DISPERSION_ACTIVE, updated.alertLevel());
        assertEquals(4, updated.currentState().activeShuttleBuses());
        assertEquals(0.89, updated.currentState().pedestrianDensityRatio(), 0.001);
    }
}
