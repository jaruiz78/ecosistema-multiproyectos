package com.corp.ecosystem.natura2000;

import com.corp.ecosystem.natura2000.application.NationalParkEcoService;
import com.corp.ecosystem.natura2000.domain.NationalParkEcoZoneTwin;
import com.corp.ecosystem.natura2000.domain.port.NationalParkZoneRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class NationalParkEcoZoneTwinTest {

    static class InMemoryZoneRepository implements NationalParkZoneRepositoryPort {
        private final Map<NationalParkEcoZoneTwin.ZoneId, NationalParkEcoZoneTwin> storage = new ConcurrentHashMap<>();

        @Override
        public NationalParkEcoZoneTwin save(NationalParkEcoZoneTwin zone) {
            storage.put(zone.id(), zone);
            return zone;
        }

        @Override
        public Optional<NationalParkEcoZoneTwin> findById(NationalParkEcoZoneTwin.ZoneId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryZoneRepository repository = new InMemoryZoneRepository();
    private final NationalParkEcoService service = new NationalParkEcoService(repository);

    @Test
    @DisplayName("Debe activar cierre temporal por exceso de aforo o perturbación biológica crítica")
    void shouldTriggerTemporaryEcoClosure() {
        NationalParkEcoZoneTwin zone = service.monitorEcoZone(
                "oapn-parques-nacionales",
                "Parque Nacional de Doñana - Red Natura 2000",
                0x88390cb307fffffL,
                450,
                300, // 450 > 300
                0.82,
                false
        );

        assertNotNull(zone.id());
        assertEquals(NationalParkEcoZoneTwin.EcoZoneAccessStatus.TEMPORARY_ECO_CLOSURE, zone.status());
        assertTrue(zone.metrics().isEcoLimitExceeded());
    }
}
