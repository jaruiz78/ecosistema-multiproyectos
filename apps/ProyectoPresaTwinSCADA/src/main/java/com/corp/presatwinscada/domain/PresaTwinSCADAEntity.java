package com.corp.presatwinscada.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: PresaTwinSCADA
 */
public record PresaTwinSCADAEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public PresaTwinSCADAEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
