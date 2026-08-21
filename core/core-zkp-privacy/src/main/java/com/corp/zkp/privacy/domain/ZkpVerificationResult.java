package com.corp.zkp.privacy.domain;

import java.util.Objects;

/**
 * Resultado formal de la verificación de una prueba ZKP.
 *
 * @param isValid               Indica si la prueba es matemáticamente válida
 * @param reason                Descripción del resultado de verificación
 * @param verificationTimeNanos Tiempo de cómputo en nanosegundos
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record ZkpVerificationResult(
        boolean isValid,
        String reason,
        long verificationTimeNanos
) {
    public ZkpVerificationResult {
        Objects.requireNonNull(reason, "reason no puede ser nula");
        if (verificationTimeNanos < 0) {
            throw new IllegalArgumentException("verificationTimeNanos no puede ser negativo");
        }
    }

    public static ZkpVerificationResult success(long nanos) {
        return new ZkpVerificationResult(true, "Prueba ZKP verificada con éxito (Soundness & Completeness garantizadas)", nanos);
    }

    public static ZkpVerificationResult failure(String reason, long nanos) {
        return new ZkpVerificationResult(false, reason, nanos);
    }
}
