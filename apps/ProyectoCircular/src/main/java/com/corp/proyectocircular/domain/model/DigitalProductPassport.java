package com.corp.proyectocircular.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: DigitalProductPassport.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record DigitalProductPassport(
    String passportId, String materialBatch, double recycledContentPercent, String qrUri,
    Instant timestamp
) {
    public DigitalProductPassport {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(recycledContentPercent >= 0.0 && recycledContentPercent <= 100.0)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en DigitalProductPassport");
        }
    }
}
