package com.corp.proyectosalud.application;

import com.corp.proyectosalud.domain.ZkClinicalTrialLedgerEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas para {@link FederatedClinicalTrialPrivacyLedgerService}.
 * Verifica agregación homomórfica Pedersen, ZKP Range Proofs, cifrado post-cuántico y Virtual Threads.
 */
class FederatedClinicalTrialPrivacyLedgerServiceTest {

    private final FederatedClinicalTrialPrivacyLedgerService service =
            new FederatedClinicalTrialPrivacyLedgerService();

    @Test
    @DisplayName("Debe procesar y certificar un lote de ensayo clínico con ZKP y PQC exitosamente")
    void shouldProcessConfidentialTrialBatch() {
        String cohortId = "COHORT-ONCO-2026";
        long metric1 = 45L;
        long metric2 = 60L;
        long minAllowed = 10L;
        long maxAllowed = 100L;

        ZkClinicalTrialLedgerEntry entry = service.processConfidentialTrialBatch(
                cohortId, metric1, metric2, minAllowed, maxAllowed
        );

        assertNotNull(entry);
        assertEquals(cohortId, entry.studyCohortId());
        assertNotNull(entry.aggregateCommitmentHex());
        assertTrue(entry.isRangeProofValid(), "La prueba de rango ZKP debe ser válida");
        assertNotNull(entry.pqcCipherHex());
        assertTrue(entry.verificationLatencyNanos() >= 0);
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia con 40 Virtual Threads sin contención de hilos portadores")
    void shouldExecuteConcurrentlyUnderVirtualThreads() throws InterruptedException {
        int threads = 40;
        AtomicInteger successCounter = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                final long val1 = 20 + (i % 30);
                final long val2 = 30 + (i % 40);
                executor.submit(() -> {
                    ZkClinicalTrialLedgerEntry entry = service.processConfidentialTrialBatch(
                            "COHORT-CARDIO", val1, val2, 10L, 150L
                    );
                    if (entry.isRangeProofValid() && entry.pqcCipherHex() != null) {
                        successCounter.incrementAndGet();
                    }
                });
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        assertEquals(threads, successCounter.get(), "Todos los Virtual Threads deben procesar lotes ZKP y PQC");
    }
}
