package com.corp.proyectodefensa.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Agregado de Dominio para Sobres Tácticos Post-Cuánticos (PQC Kyber / ML-KEM).
 * Modela la transmisión segura de telemetría y órdenes tácticas en entornos Air-Gapped.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public record KyberSecurityEnvelope(
        String envelopeId,
        String tenantId,
        String sourceNodeId,
        String targetNodeId,
        String ciphertextKyberB64,
        String sharedSecretDigest,
        int airGappedHopCount,
        Instant timestamp
) {
    public KyberSecurityEnvelope {
        Objects.requireNonNull(envelopeId, "El envelopeId es obligatorio");
        Objects.requireNonNull(tenantId, "El tenantId es obligatorio");
        Objects.requireNonNull(sourceNodeId, "El sourceNodeId es obligatorio");
        Objects.requireNonNull(targetNodeId, "El targetNodeId es obligatorio");
        Objects.requireNonNull(ciphertextKyberB64, "El ciphertextKyberB64 es obligatorio");
        Objects.requireNonNull(sharedSecretDigest, "El sharedSecretDigest es obligatorio");
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (airGappedHopCount < 0) {
            throw new IllegalArgumentException("El número de saltos air-gapped debe ser no-negativo (Hoare Precondition)");
        }
    }
}
