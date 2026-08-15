package com.corp.ecosystem.mice;

import com.corp.ecosystem.mice.application.MiceCongressService;
import com.corp.ecosystem.mice.domain.MiceEventCongressTwin;
import com.corp.ecosystem.mice.domain.port.MiceCongressRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class MiceEventCongressTwinTest {

    static class InMemoryMiceRepository implements MiceCongressRepositoryPort {
        private final Map<MiceEventCongressTwin.CongressId, MiceEventCongressTwin> storage = new ConcurrentHashMap<>();

        @Override
        public MiceEventCongressTwin save(MiceEventCongressTwin event) {
            storage.put(event.id(), event);
            return event;
        }

        @Override
        public Optional<MiceEventCongressTwin> findById(MiceEventCongressTwin.CongressId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryMiceRepository repository = new InMemoryMiceRepository();
    private final MiceCongressService service = new MiceCongressService(repository);

    @Test
    @DisplayName("Debe registrar y clasificar feria internacional como TIER1_INTERNATIONAL_FLAGSHIP")
    void shouldRegisterTier1Congress() {
        MiceEventCongressTwin event = service.createCongress(
                "fira-barcelona-mice",
                "Mobile World Congress 2026",
                "Fira Gran Via",
                105000,
                2400,
                520000000.0,
                96.5
        );

        assertNotNull(event.id());
        assertEquals(MiceEventCongressTwin.EventOperationalStatus.TIER1_INTERNATIONAL_FLAGSHIP, event.status());
    }
}
