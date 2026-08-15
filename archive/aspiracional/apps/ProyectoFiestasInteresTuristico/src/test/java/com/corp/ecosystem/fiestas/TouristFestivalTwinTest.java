package com.corp.ecosystem.fiestas;

import com.corp.ecosystem.fiestas.application.TouristFestivalService;
import com.corp.ecosystem.fiestas.domain.TouristFestivalTwin;
import com.corp.ecosystem.fiestas.domain.port.TouristFestivalRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class TouristFestivalTwinTest {

    static class InMemoryFestivalRepository implements TouristFestivalRepositoryPort {
        private final Map<TouristFestivalTwin.FestivalId, TouristFestivalTwin> storage = new ConcurrentHashMap<>();

        @Override
        public TouristFestivalTwin save(TouristFestivalTwin festival) {
            storage.put(festival.id(), festival);
            return festival;
        }

        @Override
        public Optional<TouristFestivalTwin> findById(TouristFestivalTwin.FestivalId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryFestivalRepository repository = new InMemoryFestivalRepository();
    private final TouristFestivalService service = new TouristFestivalService(repository);

    @Test
    @DisplayName("Debe clasificar seguridad como LEVEL_GREEN_CONTROLLED con aforo y corredores de evacuación despejados")
    void shouldPlanSafeFestival() {
        TouristFestivalTwin festival = service.scheduleFestival(
                "ayuntamiento-valencia",
                "Las Fallas de Valencia - Nit del Foc",
                "Fiesta de Interés Turístico Internacional",
                85000,
                120000, // 85k < 120k
                14,
                true
        );

        assertNotNull(festival.id());
        assertEquals(TouristFestivalTwin.FestivalSecurityLevel.LEVEL_GREEN_CONTROLLED, festival.securityLevel());
        assertFalse(festival.metrics().isOvercrowdedRisk());
    }
}
