package com.corp.proyectologistica.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: AutonomousFleetRoute.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record AutonomousFleetRoute(
    String routeId, String h3OriginHex, String h3DestinationHex, double totalDistanceKm,
    Instant timestamp
) {
    public AutonomousFleetRoute {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(totalDistanceKm >= 0.0)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en AutonomousFleetRoute");
        }
    }
}
