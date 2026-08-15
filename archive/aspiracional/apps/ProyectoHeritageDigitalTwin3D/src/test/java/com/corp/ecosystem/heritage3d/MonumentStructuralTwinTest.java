package com.corp.ecosystem.heritage3d;

import com.corp.ecosystem.heritage3d.application.MonumentTwinService;
import com.corp.ecosystem.heritage3d.domain.MonumentStructuralTwin;
import com.corp.ecosystem.heritage3d.domain.port.MonumentTwinRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class MonumentStructuralTwinTest {

    static class InMemoryMonumentRepository implements MonumentTwinRepositoryPort {
        private final Map<MonumentStructuralTwin.MonumentId, MonumentStructuralTwin> storage = new ConcurrentHashMap<>();

        @Override
        public MonumentStructuralTwin save(MonumentStructuralTwin twin) {
            storage.put(twin.id(), twin);
            return twin;
        }

        @Override
        public Optional<MonumentStructuralTwin> findById(MonumentStructuralTwin.MonumentId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryMonumentRepository repository = new InMemoryMonumentRepository();
    private final MonumentTwinService service = new MonumentTwinService(repository);

    @Test
    @DisplayName("Debe clasificar como STABLE_NORMAL un monumento con parámetros dentro del margen estructural")
    void shouldAnalyzeStableMonument() {
        MonumentStructuralTwin twin = service.recordScan(
                "patrimonio-nacional-alhambragranada",
                "Alhambra de Granada - Patio de los Leones",
                125_000_000L,
                0.24, // 0.24mm < 2.0mm
                12.5, // 12.5% < 25%
                8.4,
                2.0
        );

        assertNotNull(twin.id());
        assertEquals(MonumentStructuralTwin.ConservationUrgency.STABLE_NORMAL, twin.urgencyLevel());
        assertFalse(twin.metrics().isDisplacementCritical());
    }
}
