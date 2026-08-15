package com.corp.ecosystem.paradores;

import com.corp.ecosystem.paradores.application.HistoricParadorService;
import com.corp.ecosystem.paradores.domain.HistoricParadorTwin;
import com.corp.ecosystem.paradores.domain.port.ParadorRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class HistoricParadorTwinTest {

    static class InMemoryParadorRepository implements ParadorRepositoryPort {
        private final Map<HistoricParadorTwin.ParadorId, HistoricParadorTwin> storage = new ConcurrentHashMap<>();

        @Override
        public HistoricParadorTwin save(HistoricParadorTwin parador) {
            storage.put(parador.id(), parador);
            return parador;
        }

        @Override
        public Optional<HistoricParadorTwin> findById(HistoricParadorTwin.ParadorId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryParadorRepository repository = new InMemoryParadorRepository();
    private final HistoricParadorService service = new HistoricParadorService(repository);

    @Test
    @DisplayName("Debe validar conservación histórica y eficiencia bioclimática en Parador")
    void shouldOptimizeHeritageEnergy() {
        HistoricParadorTwin parador = service.monitorHistoricParador(
                "paradores-de-espana",
                "Parador de Santiago - Hostal dos Reis Católicos",
                "Monumento Nacional Siglo XV",
                21.5,
                22.0,
                45.0,
                52.0 // 52% dentro de [45%, 60%]
        );

        assertNotNull(parador.id());
        assertEquals(HistoricParadorTwin.ParadorOperationalStatus.HERITAGE_ENERGY_OPTIMIZED, parador.status());
        assertTrue(parador.thermalProfile().isHeritageConservationSafe());
    }
}
