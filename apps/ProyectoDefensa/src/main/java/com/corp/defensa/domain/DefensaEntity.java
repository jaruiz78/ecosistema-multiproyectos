package com.corp.defensa.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: Defensa
 */
public record DefensaEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public DefensaEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
