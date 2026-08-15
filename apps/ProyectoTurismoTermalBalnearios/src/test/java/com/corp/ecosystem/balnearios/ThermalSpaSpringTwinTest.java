package com.corp.ecosystem.balnearios;

import com.corp.ecosystem.balnearios.application.ThermalSpaService;
import com.corp.ecosystem.balnearios.domain.ThermalSpaSpringTwin;
import com.corp.ecosystem.balnearios.domain.port.ThermalSpaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ThermalSpaSpringTwinTest {

    static class InMemorySpaRepository implements ThermalSpaRepositoryPort {
        private final Map<ThermalSpaSpringTwin.SpaSpringId, ThermalSpaSpringTwin> storage = new ConcurrentHashMap<>();

        @Override
        public ThermalSpaSpringTwin save(ThermalSpaSpringTwin twin) {
            storage.put(twin.id(), twin);
            return twin;
        }

        @Override
        public Optional<ThermalSpaSpringTwin> findById(ThermalSpaSpringTwin.SpaSpringId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemorySpaRepository repository = new InMemorySpaRepository();
    private final ThermalSpaService service = new ThermalSpaService(repository);

    @Test
    @DisplayName("Debe clasificar balneario histórico en estado BALNEOTHERAPY_OPTIMAL")
    void shouldClassifyOptimalSpaSpring() {
        ThermalSpaSpringTwin twin = service.monitorThermalSpring(
                "diputacion-ourense-termal",
                "Termas de Outariz y Burgas de Ourense",
                "Galicia",
                38.5, // 38.5°C óptimo
                1420.0,
                18.5,
                45,
                100 // 45 < 100
        );

        assertNotNull(twin.id());
        assertEquals(ThermalSpaSpringTwin.SpaOperationalStatus.BALNEOTHERAPY_OPTIMAL, twin.status());
        assertTrue(twin.metrics().isTemperatureOptimal());
        assertFalse(twin.metrics().isCapacityExceeded());
    }
}
