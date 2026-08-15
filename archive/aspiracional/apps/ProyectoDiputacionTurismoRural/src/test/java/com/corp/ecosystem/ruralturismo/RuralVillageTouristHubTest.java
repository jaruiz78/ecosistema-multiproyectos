package com.corp.ecosystem.ruralturismo;

import com.corp.ecosystem.ruralturismo.application.RuralTurismoService;
import com.corp.ecosystem.ruralturismo.domain.RuralVillageTouristHub;
import com.corp.ecosystem.ruralturismo.domain.port.RuralHubRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class RuralVillageTouristHubTest {

    static class InMemoryRuralHubRepository implements RuralHubRepositoryPort {
        private final Map<RuralVillageTouristHub.HubId, RuralVillageTouristHub> storage = new ConcurrentHashMap<>();

        @Override
        public RuralVillageTouristHub save(RuralVillageTouristHub hub) {
            storage.put(hub.id(), hub);
            return hub;
        }

        @Override
        public Optional<RuralVillageTouristHub> findById(RuralVillageTouristHub.HubId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryRuralHubRepository repository = new InMemoryRuralHubRepository();
    private final RuralTurismoService service = new RuralTurismoService(repository);

    @Test
    @DisplayName("Debe evaluar municipio rural y clasificarlo en HIGH_TRACTION_ECOTURISMO")
    void shouldEvaluateHighTractionVillage() {
        RuralVillageTouristHub hub = service.registerVillageHub(
                "diputacion-huesca",
                "Aínsa-Sobrarbe",
                "Huesca",
                2200,
                35,  // Casas rurales
                18,  // Senderos
                78.5, // 78.5% ocupación (>65%)
                4500000.0
        );

        assertNotNull(hub.id());
        assertEquals(RuralVillageTouristHub.RevitalizationStatus.HIGH_TRACTION_ECOTURISMO, hub.status());
    }
}
