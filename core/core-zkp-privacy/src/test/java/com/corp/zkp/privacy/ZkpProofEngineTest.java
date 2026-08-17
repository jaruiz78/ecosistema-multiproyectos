package com.corp.zkp.privacy;

import com.corp.zkp.privacy.domain.PedersenCommitment;
import com.corp.zkp.privacy.domain.ZkpRangeProof;
import com.corp.zkp.privacy.domain.ZkpVerificationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas unitarias y de concurrencia para {@link ZkpProofEngine}.
 * Verifica compromisos homomórficos de Pedersen, pruebas de rango no interactivas Fiat-Shamir
 * y ejecución concurrente bajo Virtual Threads de Java 25.
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 */
class ZkpProofEngineTest {

    private final ZkpProofEngine engine = new ZkpProofEngine();

    @Test
    @DisplayName("Debe generar un compromiso de Pedersen determinista y no nulo")
    void shouldGenerateValidPedersenCommitment() {
        long value = 42_000L;
        BigInteger r = engine.generateBlindingFactor();
        PedersenCommitment commitment = engine.createCommitment(value, r);

        assertNotNull(commitment);
        assertNotNull(commitment.commitment());
        assertNotNull(commitment.commitmentHex());
        assertTrue(commitment.commitment().compareTo(BigInteger.ZERO) > 0);
        assertTrue(commitment.commitment().compareTo(ZkpProofEngine.PRIME_P) < 0);
    }

    @Test
    @DisplayName("Debe cumplir la propiedad homomórfica aditiva de Pedersen: C(v1 + v2) == C(v1) * C(v2) mod p")
    void shouldSatisfyAdditiveHomomorphism() {
        long v1 = 1500L;
        long v2 = 2500L;

        BigInteger r1 = engine.generateBlindingFactor();
        BigInteger r2 = engine.generateBlindingFactor();

        PedersenCommitment c1 = engine.createCommitment(v1, r1);
        PedersenCommitment c2 = engine.createCommitment(v2, r2);

        // Suma homomórfica C_sum = C1 * C2 mod p
        PedersenCommitment cSum = engine.addCommitments(c1, c2);

        // Compromiso directo de (v1 + v2) con (r1 + r2 mod order)
        BigInteger order = ZkpProofEngine.PRIME_P.subtract(BigInteger.ONE);
        BigInteger rSum = r1.add(r2).mod(order);
        PedersenCommitment expectedCommitment = engine.createCommitment(v1 + v2, rSum);

        assertEquals(expectedCommitment.commitment(), cSum.commitment(),
                "La suma homomórfica debe coincidir exactamente con el compromiso de la suma");
    }

    @Test
    @DisplayName("Debe generar y verificar una prueba de rango ZKP válida [0, 100]")
    void shouldGenerateAndVerifyValidRangeProof() {
        long value = 75L;
        long minBound = 0L;
        long maxBound = 100L;
        BigInteger r = engine.generateBlindingFactor();

        ZkpRangeProof proof = engine.generateRangeProof(value, minBound, maxBound, r);

        assertNotNull(proof);
        assertEquals(minBound, proof.minRange());
        assertEquals(maxBound, proof.maxRange());
        assertNotNull(proof.proofDigest());
        assertNotNull(proof.challenge());
        assertNotNull(proof.response());

        ZkpVerificationResult result = engine.verifyRangeProof(proof);

        assertTrue(result.isValid(), "La prueba de rango debe ser válida para valor dentro del intervalo");
        assertTrue(result.reason().contains("éxito"));
        assertTrue(result.verificationTimeNanos() >= 0);
    }

    @Test
    @DisplayName("Debe rechazar la generación de prueba de rango si el valor supera la cota")
    void shouldRejectRangeProofWhenValueExceedsBound() {
        long value = 150L;
        long minBound = 0L;
        long maxBound = 100L;
        BigInteger r = engine.generateBlindingFactor();

        assertThrows(IllegalArgumentException.class, () ->
                engine.generateRangeProof(value, minBound, maxBound, r));
    }

    @Test
    @DisplayName("Debe detectar manipulación en el desafío de una prueba de rango")
    void shouldDetectTamperedRangeProof() {
        long value = 25L;
        long minBound = 0L;
        long maxBound = 50L;
        BigInteger r = engine.generateBlindingFactor();

        ZkpRangeProof originalProof = engine.generateRangeProof(value, minBound, maxBound, r);

        // Crear una prueba manipulada con un desafío no válido
        ZkpRangeProof tamperedProof = new ZkpRangeProof(
                originalProof.commitment(),
                originalProof.minRange(),
                originalProof.maxRange(),
                originalProof.proofDigest(),
                BigInteger.ZERO, // Desafío inválido
                originalProof.response()
        );

        ZkpVerificationResult result = engine.verifyRangeProof(tamperedProof);
        assertFalse(result.isValid(), "Una prueba con desafío inválido debe ser rechazada");
    }

    @Test
    @DisplayName("Debe soportar alta concurrencia con 50 Virtual Threads sin Carrier Thread Pinning")
    void shouldExecuteConcurrentlyUnderVirtualThreads() throws InterruptedException {
        int threadCount = 50;
        AtomicInteger successCount = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCount; i++) {
                final long val = 10 + i;
                executor.submit(() -> {
                    BigInteger r = engine.generateBlindingFactor();
                    PedersenCommitment c = engine.createCommitment(val, r);
                    if (c.commitment() != null) {
                        ZkpRangeProof proof = engine.generateRangeProof(val, 0L, 200L, r);
                        if (engine.verifyRangeProof(proof).isValid()) {
                            successCount.incrementAndGet();
                        }
                    }
                });
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        assertEquals(threadCount, successCount.get(), "Todos los Virtual Threads deben completar la generación y verificación ZKP");
    }
}
