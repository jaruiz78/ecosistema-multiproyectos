package com.corp.ecosystem.pharmacold;

import com.corp.ecosystem.pharmacold.application.PharmaColdChainService;
import com.corp.ecosystem.pharmacold.domain.PharmaShipmentBatch;
import com.corp.ecosystem.pharmacold.domain.port.PharmaBatchRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoPharmaColdChain.
 */
class PharmaShipmentBatchTest {

    static class InMemoryPharmaBatchRepository implements PharmaBatchRepositoryPort {
        private final Map<PharmaShipmentBatch.BatchId, PharmaShipmentBatch> storage = new ConcurrentHashMap<>();

        @Override
        public PharmaShipmentBatch save(PharmaShipmentBatch batch) {
            storage.put(batch.id(), batch);
            return batch;
        }

        @Override
        public Optional<PharmaShipmentBatch> findById(PharmaShipmentBatch.BatchId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryPharmaBatchRepository repository = new InMemoryPharmaBatchRepository();
    private final PharmaColdChainService service = new PharmaColdChainService(repository);

    @Test
    @DisplayName("Debe registrar un lote de péptidos GLP-1 en estado IN_TRANSIT_OPTIMAL")
    void shouldRegisterGlp1BatchInOptimalState() {
        PharmaShipmentBatch batch = service.registerBatch(
                "pharma-novartis",
                "Semaglutide 2.4mg GLP-1",
                PharmaShipmentBatch.DrugCategory.GLP1_PEPTIDE,
                2.0,  // Min 2°C
                8.0,  // Max 8°C
                5.0,  // Max 5% pérdida de potencia permitida
                85.0  // 85 kJ/mol Ea
        );

        assertNotNull(batch.id());
        assertEquals(PharmaShipmentBatch.BatchReleaseStatus.IN_TRANSIT_OPTIMAL, batch.releaseStatus());
        assertEquals(0.0, batch.currentPotencyLossPct());
    }

    @Test
    @DisplayName("Debe detectar excursión térmica severa y degradar potencia según cinética de Arrhenius")
    void shouldDetectThermalExcursionAndDegradePotency() {
        PharmaShipmentBatch batch = service.registerBatch(
                "pharma-pfizer",
                "mRNA BioNTech Vaccine",
                PharmaShipmentBatch.DrugCategory.MRNA_VACCINE,
                2.0,
                8.0,
                2.0, // Max 2% pérdida permitida
                95.0
        );

        // Excursión extrema a 25°C
        PharmaShipmentBatch updated = service.recordTelemetry(batch.id(), 25.0, 60.0);

        assertTrue(updated.currentPotencyLossPct() > 0.0);
        assertTrue(updated.readings().getFirst().isTemperatureExcursion());
    }
}
