package com.corp.ecosystem.senderismo;

import com.corp.ecosystem.senderismo.application.HikingTrailService;
import com.corp.ecosystem.senderismo.domain.HikingTrailSegmentTwin;
import com.corp.ecosystem.senderismo.domain.port.HikingTrailRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class HikingTrailSegmentTwinTest {

    static class InMemoryTrailRepository implements HikingTrailRepositoryPort {
        private final Map<HikingTrailSegmentTwin.TrailSegmentId, HikingTrailSegmentTwin> storage = new ConcurrentHashMap<>();

        @Override
        public HikingTrailSegmentTwin save(HikingTrailSegmentTwin trail) {
            storage.put(trail.id(), trail);
            return trail;
        }

        @Override
        public Optional<HikingTrailSegmentTwin> findById(HikingTrailSegmentTwin.TrailSegmentId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryTrailRepository repository = new InMemoryTrailRepository();
    private final HikingTrailService service = new HikingTrailService(repository);

    @Test
    @DisplayName("Debe activar TRAIL_CLOSED_EMERGENCY ante alerta de vientos severos o desprendimientos")
    void shouldCloseTrailOnSevereWeather() {
        HikingTrailSegmentTwin trail = service.updateTrailSegment(
                "fedme-montana-pirineos",
                "GR-11 Senda Pirenaica - Refugio de Goriz a Pineta",
                14500,
                1120,
                38,
                82.0, // 82 km/h > 75 km/h
                12.0,
                true, // Alerta activa
                2
        );

        assertNotNull(trail.id());
        assertEquals(HikingTrailSegmentTwin.TrailStatus.TRAIL_CLOSED_EMERGENCY, trail.status());
        assertTrue(trail.metrics().isSevereWeatherDanger());
    }
}
