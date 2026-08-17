package com.corp.proyectosalud.domain;

import java.util.Objects;

/**
 * Entrada inmutable de auditoría de ensayo clínico con privacidad Zero-Knowledge y blindaje Post-Cuántico.
 *
 * @param entryId                Identificador único de la entrada
 * @param studyCohortId          Identificador de la cohorte del ensayo
 * @param aggregateCommitmentHex  Representación hexadecimal del compromiso de Pedersen agregado
 * @param isRangeProofValid      Indica si la prueba de rango ZKP fue validada exitosamente
 * @param pqcCipherHex           Cifrado post-cuántico (NIST ML-KEM) del reporte clínico
 * @param verificationLatencyNanos Latencia de verificación matemática en nanosegundos
 * @param timestampMs            Marca temporal
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 */
public record ZkClinicalTrialLedgerEntry(
        String entryId,
        String studyCohortId,
        String aggregateCommitmentHex,
        boolean isRangeProofValid,
        String pqcCipherHex,
        long verificationLatencyNanos,
        long timestampMs
) {
    public ZkClinicalTrialLedgerEntry {
        Objects.requireNonNull(entryId, "entryId no puede ser nulo");
        Objects.requireNonNull(studyCohortId, "studyCohortId no puede ser nulo");
        Objects.requireNonNull(aggregateCommitmentHex, "aggregateCommitmentHex no puede ser nulo");
        Objects.requireNonNull(pqcCipherHex, "pqcCipherHex no puede ser nulo");
        if (verificationLatencyNanos < 0 || timestampMs < 0) {
            throw new IllegalArgumentException("Métricas temporales no pueden ser negativas");
        }
    }
}
