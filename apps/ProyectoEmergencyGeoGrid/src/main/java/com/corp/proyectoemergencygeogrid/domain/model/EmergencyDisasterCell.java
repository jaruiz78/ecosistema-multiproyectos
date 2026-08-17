package com.corp.proyectoemergencygeogrid.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: EmergencyDisasterCell.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record EmergencyDisasterCell(
    String cellHexH3, String disasterType, int severityLevel, int populationAtRisk,
    Instant timestamp
) {
    public EmergencyDisasterCell {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(severityLevel >= 1 && severityLevel <= 5)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en EmergencyDisasterCell");
        }
    }
}
