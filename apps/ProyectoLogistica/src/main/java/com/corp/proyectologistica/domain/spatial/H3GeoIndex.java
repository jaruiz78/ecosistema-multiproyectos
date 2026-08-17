package com.corp.proyectologistica.domain.spatial;

/**
 * Spatial Index for H3 coordinates. O(1) retrieval guarantees.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record H3GeoIndex(String h3CellId, double demandSurgeFactor) {
    public H3GeoIndex {
        java.util.Objects.requireNonNull(h3CellId, "Invariante de Hoare: 'h3CellId' no puede ser nulo en H3GeoIndex");
    }
}
