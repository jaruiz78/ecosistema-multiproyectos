package com.corp.formal.verification;

import com.corp.formal.verification.domain.HoareTriple;
import com.corp.formal.verification.domain.StateInvariant;
import com.corp.formal.verification.domain.VerificationCertificate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas unitarias y de concurrencia para {@link HoareInvariantVerifier}.
 * Verifica ternas de Hoare, invariantes inductivos de bucle, transiciones relacionales
 * y concurrencia bajo Virtual Threads de Java 25.
 *
 * @see docs/formacion_ecosistema/modulo_1_sistemas_distribuidos_concurrencia/01_fundamentos_lamport_raft.md
 */
class HoareInvariantVerifierTest {

    private final HoareInvariantVerifier verifier = new HoareInvariantVerifier();

    // Registro de estado de ejemplo: Balance y Nivel de Agua/Energía
    record SystemState(long balance, double resourceLevel) {}

    @Test
    @DisplayName("Debe verificar con éxito una terna de Hoare válida {P} C {Q} y sus invariantes")
    void shouldVerifyValidHoareTriple() {
        StateInvariant<SystemState> nonNegativeBalance = new StateInvariant<>(
                "No-Negativo",
                "El balance debe ser mayor o igual a cero",
                s -> s.balance() >= 0
        );

        StateInvariant<SystemState> resourceCap = new StateInvariant<>(
                "Cota de Recurso",
                "El nivel de recurso debe estar entre 0.0 y 100.0",
                s -> s.resourceLevel() >= 0.0 && s.resourceLevel() <= 100.0
        );

        HoareTriple<SystemState> debitTriple = new HoareTriple<>(
                "Contrato de Débito de Saldo",
                s -> s.balance() >= 50, // Precondición: saldo suficiente
                s -> new SystemState(s.balance() - 50, Math.min(100.0, s.resourceLevel() + 10.0)),
                (s0, s1) -> s1.balance() == s0.balance() - 50 && s1.resourceLevel() > s0.resourceLevel()
        );

        SystemState initialState = new SystemState(200, 50.0);
        VerificationCertificate cert = verifier.verifyTriple(
                debitTriple,
                initialState,
                List.of(nonNegativeBalance, resourceCap)
        );

        assertNotNull(cert);
        assertTrue(cert.verified(), "El certificado debe ser válido");
        assertEquals("Contrato de Débito de Saldo", cert.contractName());
        assertEquals(2, cert.checkedInvariants().size());
        assertNotNull(cert.proofDigest());
        assertTrue(cert.executionTimeNanos() >= 0);
    }

    @Test
    @DisplayName("Debe fallar si no se satisface la precondición inicial")
    void shouldFailWhenPreconditionViolated() {
        HoareTriple<SystemState> debitTriple = new HoareTriple<>(
                "Contrato Débito Insuficiente",
                s -> s.balance() >= 500, // Precondición insatisfecha
                s -> new SystemState(s.balance() - 500, s.resourceLevel()),
                (s0, s1) -> s1.balance() == s0.balance() - 500
        );

        SystemState initialState = new SystemState(100, 20.0);
        VerificationCertificate cert = verifier.verifyTriple(debitTriple, initialState, List.of());

        assertFalse(cert.verified());
        assertTrue(cert.proofDigest().contains("Precondición P no satisfecha"));
    }

    @Test
    @DisplayName("Debe verificar la inducción de un bucle formal preservando el invariante")
    void shouldVerifyInductiveLoop() {
        StateInvariant<Integer> nonNegativeInv = new StateInvariant<>(
                "Entero Positivo",
                "El contador debe ser no negativo",
                val -> val >= 0
        );

        // Bucle formal que decrementa de 10 a 0
        VerificationCertificate cert = verifier.verifyInductiveLoop(
                "Decrement Loop Induction",
                10,
                val -> val > 0,
                val -> val - 1,
                nonNegativeInv,
                20
        );

        assertTrue(cert.verified(), "La inducción del bucle debe ser formalmente correcta");
        assertNotNull(cert.proofDigest());
        assertEquals(64, cert.proofDigest().length(), "El digest SHA-256 debe tener 64 caracteres hexadecimales");
    }

    @Test
    @DisplayName("Debe detectar violación de invariante durante el bucle")
    void shouldDetectInvariantViolationInLoop() {
        StateInvariant<Integer> strictlyPositiveInv = new StateInvariant<>(
                "Estrictamente Positivo",
                "El contador debe ser > 0",
                val -> val > 0
        );

        // Bucle que lleva a 0 violando val > 0
        VerificationCertificate cert = verifier.verifyInductiveLoop(
                "Flawed Loop Induction",
                2,
                val -> val >= 0,
                val -> val - 1,
                strictlyPositiveInv,
                10
        );

        assertFalse(cert.verified());
        assertTrue(cert.proofDigest().contains("Invariante violado"));
    }

    @Test
    @DisplayName("Debe verificar transiciones de estado relacionales")
    void shouldVerifyStateTransition() {
        SystemState s1 = new SystemState(100, 20.0);
        SystemState s2 = new SystemState(80, 40.0);

        VerificationCertificate cert = verifier.verifyStateTransition(
                "Transición de Carga",
                s1,
                s2,
                List.of(new StateInvariant<>("Conservación", "Nivel positivo", s -> s.resourceLevel() > 0)),
                (from, to) -> to.resourceLevel() > from.resourceLevel() && to.balance() < from.balance()
        );

        assertTrue(cert.verified());
        assertNotNull(cert.proofDigest());
    }

    @Test
    @DisplayName("Debe ejecutar concurrentemente en 50 Virtual Threads sin bloqueo de Carrier Threads")
    void shouldExecuteConcurrentlyUnderVirtualThreads() throws InterruptedException {
        int threads = 50;
        AtomicInteger successCounter = new AtomicInteger(0);

        StateInvariant<SystemState> inv = new StateInvariant<>("Inv", "Inv", s -> s.balance() >= 0);
        HoareTriple<SystemState> triple = new HoareTriple<>(
                "Concurrencia Loom",
                s -> s.balance() > 0,
                s -> new SystemState(s.balance() + 1, s.resourceLevel()),
                (s0, s1) -> s1.balance() == s0.balance() + 1
        );

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    VerificationCertificate cert = verifier.verifyTriple(
                            triple,
                            new SystemState(100, 10.0),
                            List.of(inv)
                    );
                    if (cert.verified()) {
                        successCounter.incrementAndGet();
                    }
                });
            }
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        assertEquals(threads, successCounter.get(), "Todos los Virtual Threads deben certificar exitosamente");
    }
}
