package com.corp.coregeogridh3.domain;

/**
 * Entidad de dominio pura.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record Coregeogridh3Entity(
    java.util.UUID id,
    String state,
    long timestamp,
    double metricValue
) {
    public Coregeogridh3Entity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
