package com.corp.ecosystem.cascohistorico;

import com.corp.ecosystem.cascohistorico.application.OldTownCrowdService;
import com.corp.ecosystem.cascohistorico.domain.OldTownHeritageZoneTwin;
import com.corp.ecosystem.cascohistorico.domain.port.OldTownZoneRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class OldTownHeritageZoneTwinTest {

    static class InMemoryQuarterRepository implements OldTownZoneRepositoryPort {
        private final Map<OldTownHeritageZoneTwin.ZoneId, OldTownHeritageZoneTwin> storage = new ConcurrentHashMap<>();

        @Override
        public OldTownHeritageZoneTwin save(OldTownHeritageZoneTwin zone) {
            storage.put(zone.id(), zone);
            return zone;
        }

        @Override
        public Optional<OldTownHeritageZoneTwin> findById(OldTownHeritageZoneTwin.ZoneId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryQuarterRepository repository = new InMemoryQuarterRepository();
    private final OldTownCrowdService service = new OldTownCrowdService(repository);

    @Test
    @DisplayName("Debe activar dispersión de flujos ante sobrecarga en casco histórico")
    void shouldTriggerDispersionOnOverburdenedQuarter() {
        OldTownHeritageZoneTwin zone = service.monitorQuarter(
                "ayuntamiento-toledo-unesco",
                "Barrio de la Judería - Calle Santo Tomé",
                0x88390cb307fffffL,
                850,
                500, // 850 > 500
                74.5, // 74.5 > 70 dB
                0.45
        );

        assertNotNull(zone.id());
        assertEquals(OldTownHeritageZoneTwin.QuarterCrowdStatus.OVERBURDENED_DISPERSION_TRIGGERED, zone.status());
        assertTrue(zone.metrics().isHeritageOverburdened());
    }
}
