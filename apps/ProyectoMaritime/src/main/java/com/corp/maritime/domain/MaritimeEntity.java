package com.corp.maritime.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: Maritime
 */
public record MaritimeEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double knots; double ballast; // Hydrodynamics
) {
    public MaritimeEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
