package com.corp.proyectotokenrwa.application;

import com.corp.proyectotokenrwa.domain.GreenRwaTokenCertificate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas para {@link GreenHydrogenAssetTokenizationService}.
 * Verifica acuñación RWA, pruebas Merkle, compromisos ZKP de Pedersen y Virtual Threads.
 */
class GreenHydrogenAssetTokenizationServiceTest {

    private final GreenHydrogenAssetTokenizationService service =
            new GreenHydrogenAssetTokenizationService();

    @Test
    @DisplayName("Debe acuñar y certificar un token RWA respaldado por H2 verde con prueba Merkle válida")
    void shouldTokenizeGreenHydrogenBatch() {
        String batchId = "BATCH-H2-SOLAR-001";
        double kg = 250.0;
        double priceUsd = 6.50; // $6.50/kg

        GreenRwaTokenCertificate certificate = service.tokenizeGreenHydrogenBatch(batchId, kg, priceUsd);

        assertNotNull(certificate);
        assertEquals(batchId, certificate.hydrogenBatchId());
        assertEquals(kg, certificate.hydrogenKg());
        assertEquals(1625.0, certificate.tokenValueUsd(), 1e-4);
        assertEquals(2500.0, certificate.carbonAvoidedKgCo2(), 1e-4);
        assertNotNull(certificate.merkleRootHex());
        assertNotNull(certificate.commitmentHex());
        assertTrue(certificate.isProofVerified(), "La prueba Merkle de inclusión en el bloque debe ser válida");
    }

    @Test
    @DisplayName("Debe rechazar cantidades no positivas")
    void shouldRejectNonPositiveQuantities() {
        assertThrows(IllegalArgumentException.class, () ->
                service.tokenizeGreenHydrogenBatch("BATCH-ERR", -10.0, 5.0));
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia con 40 Virtual Threads sin contención de hilos")
    void shouldExecuteConcurrentlyUnderVirtualThreads() throws InterruptedException {
        int threads = 40;
        AtomicInteger successCounter = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                final int idx = i;
                executor.submit(() -> {
                    GreenRwaTokenCertificate cert = service.tokenizeGreenHydrogenBatch(
                            "BATCH-H2-" + idx, 50.0 + idx, 6.0
                    );
                    if (cert.isProofVerified() && cert.commitmentHex() != null) {
                        successCounter.incrementAndGet();
                    }
                });
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        assertEquals(threads, successCounter.get(), "Todos los Virtual Threads deben emitir y verificar tokens RWA");
    }
}
