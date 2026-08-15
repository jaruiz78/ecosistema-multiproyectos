package com.corp.ecosystem.microgrid;

import com.corp.ecosystem.microgrid.application.IndustrialMicrogridService;
import com.corp.ecosystem.microgrid.domain.IndustrialMicrogridNode;
import com.corp.ecosystem.microgrid.domain.port.MicrogridRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoIndustrialMicrogridMPC.
 */
class IndustrialMicrogridNodeTest {

    static class InMemoryMicrogridRepository implements MicrogridRepositoryPort {
        private final Map<IndustrialMicrogridNode.NodeId, IndustrialMicrogridNode> storage = new ConcurrentHashMap<>();

        @Override
        public IndustrialMicrogridNode save(IndustrialMicrogridNode node) {
            storage.put(node.id(), node);
            return node;
        }

        @Override
        public Optional<IndustrialMicrogridNode> findById(IndustrialMicrogridNode.NodeId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryMicrogridRepository repository = new InMemoryMicrogridRepository();
    private final IndustrialMicrogridService service = new IndustrialMicrogridService(repository);

    @Test
    @DisplayName("Debe activar descarga BESS ante tarifa pico de electricidad")
    void shouldActivateBessDischargeOnPeakTariff() {
        IndustrialMicrogridNode node = service.registerMicrogrid(
                "poligono-industrial-zona-franca",
                "Zona Franca EcoPark",
                2000.0, // 2000 kW import max
                1500.0, // 1500 kWh BESS
                800.0,  // 800 kW max descarga
                400.0   // 400 kW max corte
        );

        assertNotNull(node.id());

        // Carga de 1200 kW con tarifa pico (0.32 EUR/kWh) y frecuencia normal (50.0 Hz)
        IndustrialMicrogridNode dispatched = service.dispatchLoad(node.id(), 1200.0, 50.0, 0.32);

        assertEquals(800.0, dispatched.lastDecision().bessDischargeKw());
        assertEquals(400.0, dispatched.lastDecision().gridImportKw());
        assertTrue(dispatched.lastDecision().estimatedCostSavingsEur() > 0);
        assertFalse(dispatched.lastDecision().isFrequencySupportActive());
    }

    @Test
    @DisplayName("Debe activar soporte de frecuencia rápido ante caída a 49.80 Hz")
    void shouldActivateFastFrequencySupportOnGridDrop() {
        IndustrialMicrogridNode node = service.registerMicrogrid(
                "arcelormittal-aviles",
                "ArcelorMittal Steel Mill",
                5000.0,
                3000.0,
                2000.0,
                1000.0
        );

        // Frecuencia crítica a 49.78 Hz
        IndustrialMicrogridNode dispatched = service.dispatchLoad(node.id(), 3500.0, 49.78, 0.15);

        assertTrue(dispatched.lastDecision().isFrequencySupportActive());
        assertTrue(dispatched.lastDecision().bessDischargeKw() > 0);
    }
}
