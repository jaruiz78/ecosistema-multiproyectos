package com.corp.ecosystem.playas;

import com.corp.ecosystem.playas.application.SmartBeachService;
import com.corp.ecosystem.playas.domain.SmartBeachTwin;
import com.corp.ecosystem.playas.domain.port.SmartBeachRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class SmartBeachTwinTest {

    static class InMemorySmartBeachRepository implements SmartBeachRepositoryPort {
        private final Map<SmartBeachTwin.BeachId, SmartBeachTwin> storage = new ConcurrentHashMap<>();

        @Override
        public SmartBeachTwin save(SmartBeachTwin beach) {
            storage.put(beach.id(), beach);
            return beach;
        }

        @Override
        public Optional<SmartBeachTwin> findById(SmartBeachTwin.BeachId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemorySmartBeachRepository repository = new InMemorySmartBeachRepository();
    private final SmartBeachService service = new SmartBeachService(repository);

    @Test
    @DisplayName("Debe reportar estado GREEN_FLAG_OPTIMAL con aforo y aguas excelentes")
    void shouldReportGreenFlagOnOptimalBeachConditions() {
        SmartBeachTwin beach = service.monitorBeach(
                "ayuntamiento-san-sebastian",
                "Playa de La Concha",
                0x88390cb307fffffL,
                1200,
                3000,
                45.0, // E. coli (< 250)
                18.0, // Enterococos (< 100)
                21.5
        );

        assertNotNull(beach.id());
        assertEquals(SmartBeachTwin.BeachFlagStatus.GREEN_FLAG_OPTIMAL, beach.flagStatus());
        assertEquals(40.0, beach.capacity().occupancyPercentage(), 1e-3);
    }
}
