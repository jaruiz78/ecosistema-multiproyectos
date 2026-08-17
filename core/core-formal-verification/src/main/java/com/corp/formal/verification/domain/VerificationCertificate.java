package com.corp.formal.verification.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Certificado inmutable de verificación formal.
 *
 * @param contractName      Nombre del contrato o terna verificada
 * @param verified          Indica si todas las precondiciones, poscondiciones e invariantes se satisfacen
 * @param checkedInvariants Lista de invariantes inductivos auditados
 * @param proofDigest       Hash SHA-256 de la prueba formal y trazabilidad
 * @param executionTimeNanos Latencia de verificación en nanosegundos
 * @param timestamp         Instante de emisión del certificado
 *
 * @see docs/formacion_ecosistema/modulo_1_sistemas_distribuidos_concurrencia/01_fundamentos_lamport_raft.md
 */
public record VerificationCertificate(
        String contractName,
        boolean verified,
        List<String> checkedInvariants,
        String proofDigest,
        long executionTimeNanos,
        Instant timestamp
) {
    public VerificationCertificate {
        Objects.requireNonNull(contractName, "contractName no puede ser nulo");
        Objects.requireNonNull(checkedInvariants, "checkedInvariants no puede ser nulo");
        Objects.requireNonNull(proofDigest, "proofDigest no puede ser nulo");
        Objects.requireNonNull(timestamp, "timestamp no puede ser nulo");
        if (executionTimeNanos < 0) {
            throw new IllegalArgumentException("executionTimeNanos no puede ser negativo");
        }
    }

    public static VerificationCertificate success(String contractName, List<String> invariants, String digest, long nanos) {
        return new VerificationCertificate(contractName, true, List.copyOf(invariants), digest, nanos, Instant.now());
    }

    public static VerificationCertificate failure(String contractName, List<String> invariants, String reason, long nanos) {
        return new VerificationCertificate(contractName, false, List.copyOf(invariants), "FAILED: " + reason, nanos, Instant.now());
    }
}
