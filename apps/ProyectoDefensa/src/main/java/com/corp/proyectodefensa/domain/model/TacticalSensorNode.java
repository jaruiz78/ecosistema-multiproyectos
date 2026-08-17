package com.corp.proyectodefensa.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: TacticalSensorNode.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_10_identidad_soberana_privacidad_zkp">FACULTAD_XI: Identidad Soberana & Zero-Trust BeyondCorp</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record TacticalSensorNode(
    String sensorId, String threatClass, double signalStrengthDbm, boolean isEncrypted,
    Instant timestamp
) {
    public TacticalSensorNode {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(signalStrengthDbm <= 0.0)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en TacticalSensorNode");
        }
    }
}
