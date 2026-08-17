package com.corp.proyectologistica.domain.vrp;

/**
 * Domain record for Stochastic VRP (Vehicle Routing Problem).
 * Pure Java 25, Zero-Mockito.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record RouteRecord(String routeId, String h3GeoIndex, double priorityScore, boolean isEscrowSettled) {
    public RouteRecord {
        java.util.Objects.requireNonNull(routeId, "Invariante de Hoare: 'routeId' no puede ser nulo en RouteRecord");
    }
}
