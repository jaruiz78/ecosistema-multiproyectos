package com.corp.ecosystem.enoturismo;

import com.corp.ecosystem.enoturismo.application.WineRouteService;
import com.corp.ecosystem.enoturismo.domain.WineRouteExperienceTwin;
import com.corp.ecosystem.enoturismo.domain.port.WineRouteRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class WineRouteExperienceTwinTest {

    static class InMemoryWineRepository implements WineRouteRepositoryPort {
        private final Map<WineRouteExperienceTwin.ExperienceId, WineRouteExperienceTwin> storage = new ConcurrentHashMap<>();

        @Override
        public WineRouteExperienceTwin save(WineRouteExperienceTwin exp) {
            storage.put(exp.id(), exp);
            return exp;
        }

        @Override
        public Optional<WineRouteExperienceTwin> findById(WineRouteExperienceTwin.ExperienceId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryWineRepository repository = new InMemoryWineRepository();
    private final WineRouteService service = new WineRouteService(repository);

    @Test
    @DisplayName("Debe agendar cata exclusiva con aforo disponible en bodega")
    void shouldScheduleExclusiveTastingSession() {
        WineRouteExperienceTwin exp = service.bookWineExperience(
                "ribera-del-duero-wineries",
                "D.O. Ribera del Duero",
                "Bodegas Vega Sicilia",
                18,
                30, // 18 < 30
                98.5,
                4200.0
        );

        assertNotNull(exp.id());
        assertEquals(WineRouteExperienceTwin.TastingSessionStatus.CONFIRMED_EXCLUSIVE_TASTING, exp.status());
        assertTrue(exp.metrics().isCapacityAvailable());
    }
}
