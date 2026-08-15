package com.corp.ecosystem.baggage;

import com.corp.ecosystem.baggage.application.InterlineBaggageService;
import com.corp.ecosystem.baggage.domain.InterlineBaggageTwin;
import com.corp.ecosystem.baggage.domain.port.InterlineBaggageRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class InterlineBaggageTwinTest {

    static class InMemoryBaggageRepository implements InterlineBaggageRepositoryPort {
        private final Map<InterlineBaggageTwin.BaggageTagId, InterlineBaggageTwin> storage = new ConcurrentHashMap<>();

        @Override
        public InterlineBaggageTwin save(InterlineBaggageTwin twin) {
            storage.put(twin.id(), twin);
            return twin;
        }

        @Override
        public Optional<InterlineBaggageTwin> findById(InterlineBaggageTwin.BaggageTagId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryBaggageRepository repository = new InMemoryBaggageRepository();
    private final InterlineBaggageService service = new InterlineBaggageService(repository);

    @Test
    @DisplayName("Debe activar alerta EXPEDITED_APRON_RUNNER_ALERT cuando el tiempo restante es menor que el MCT")
    void shouldTriggerExpeditedRunnerAlert() {
        InterlineBaggageTwin twin = service.processBaggageTransfer(
                "tenant-iberia-madrid-hub",
                "IB3160",
                "AA0037",
                25, // 25 min restantes
                45, // 45 min MCT
                -68.5,
                false
        );

        assertNotNull(twin.id());
        assertEquals(InterlineBaggageTwin.BaggageTransferStatus.EXPEDITED_APRON_RUNNER_ALERT, twin.status());
        assertTrue(twin.metrics().isMishandledRisk());
    }
}
