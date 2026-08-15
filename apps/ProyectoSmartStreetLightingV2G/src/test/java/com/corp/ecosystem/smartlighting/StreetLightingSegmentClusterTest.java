package com.corp.ecosystem.smartlighting;

import com.corp.ecosystem.smartlighting.application.SmartLightingV2gService;
import com.corp.ecosystem.smartlighting.domain.StreetLightingSegmentCluster;
import com.corp.ecosystem.smartlighting.domain.port.LightingSegmentRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoSmartStreetLightingV2G.
 */
class StreetLightingSegmentClusterTest {

    static class InMemoryLightingSegmentRepository implements LightingSegmentRepositoryPort {
        private final Map<StreetLightingSegmentCluster.SegmentId, StreetLightingSegmentCluster> storage = new ConcurrentHashMap<>();

        @Override
        public StreetLightingSegmentCluster save(StreetLightingSegmentCluster cluster) {
            storage.put(cluster.id(), cluster);
            return cluster;
        }

        @Override
        public Optional<StreetLightingSegmentCluster> findById(StreetLightingSegmentCluster.SegmentId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryLightingSegmentRepository repository = new InMemoryLightingSegmentRepository();
    private final SmartLightingV2gService service = new SmartLightingV2gService(repository);

    @Test
    @DisplayName("Debe atenuar luminarias al 20% en horario nocturno sin peatones para máximo ahorro energético")
    void shouldDimLuminairesOnNoPedestrians() {
        StreetLightingSegmentCluster segment = service.registerSegment(
                "ayuntamiento-valencia-smartcity",
                0x88390cb307fffffL,
                80
        );

        assertNotNull(segment.id());

        StreetLightingSegmentCluster adjusted = service.updateSensoryConditions(
                segment.id(), 0, 0, 1.5, 2, 0.12
        );

        assertEquals(20.0, adjusted.lastDecision().targetDimmingLevelPct());
        assertEquals(80.0, adjusted.lastDecision().energySavingsPctVsNominal());
    }

    @Test
    @DisplayName("Debe encender al 100% ante afluencia de peatones e inyectar energía a red si la tarifa es pico (V2G)")
    void shouldBoostLightingAndInjectV2GOnCrowdAndPeakTariff() {
        StreetLightingSegmentCluster segment = service.registerSegment(
                "ayuntamiento-madrid-movilidad",
                0x88390cb307fffffL,
                120
        );

        // Afluencia de 25 peatones con tarifa pico (0.35 EUR/kWh) y 4 vehículos eléctricos enchufados
        StreetLightingSegmentCluster adjusted = service.updateSensoryConditions(
                segment.id(), 25, 10, 2.0, 4, 0.35
        );

        assertEquals(100.0, adjusted.lastDecision().targetDimmingLevelPct());
        assertTrue(adjusted.lastDecision().netGridInjectionKw() > 0);
    }
}
