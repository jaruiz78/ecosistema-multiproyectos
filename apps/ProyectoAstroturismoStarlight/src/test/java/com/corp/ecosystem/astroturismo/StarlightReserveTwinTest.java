package com.corp.ecosystem.astroturismo;

import com.corp.ecosystem.astroturismo.application.StarlightReserveService;
import com.corp.ecosystem.astroturismo.domain.StarlightReserveTwin;
import com.corp.ecosystem.astroturismo.domain.port.StarlightReserveRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StarlightReserveTwinTest {

    static class InMemoryStarlightRepository implements StarlightReserveRepositoryPort {
        private final Map<StarlightReserveTwin.ReserveId, StarlightReserveTwin> storage = new ConcurrentHashMap<>();

        @Override
        public StarlightReserveTwin save(StarlightReserveTwin twin) {
            storage.put(twin.id(), twin);
            return twin;
        }

        @Override
        public Optional<StarlightReserveTwin> findById(StarlightReserveTwin.ReserveId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryStarlightRepository repository = new InMemoryStarlightRepository();
    private final StarlightReserveService service = new StarlightReserveService(repository);

    @Test
    @DisplayName("Debe clasificar como PRISTINE_DARK_SKY_EXCELLENT con SQM > 21.5 y cielo despejado")
    void shouldClassifyPristineDarkSky() {
        StarlightReserveTwin twin = service.monitorDarkSky(
                "cabildo-la-palma-starlight",
                "Reserva Starlight de La Palma - Roque de los Muchachos",
                0x88390cb307fffffL,
                21.85, // 21.85 > 21.5 SQM
                5.0, // 5% nubosidad
                12.0, // 12 lm
                0.65
        );

        assertNotNull(twin.id());
        assertEquals(StarlightReserveTwin.StarlightObservationQuality.PRISTINE_DARK_SKY_EXCELLENT, twin.observationQuality());
        assertTrue(twin.metrics().isStarlightCertifiedDarkSky());
    }
}
