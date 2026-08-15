package com.corp.logistica.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: Logistica
 */
public record LogisticaEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double weightKg; String destinationH3; // Dijkstra Math
) {
    public LogisticaEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
