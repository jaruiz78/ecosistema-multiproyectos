package com.corp.caminosantiagoxacobeo.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: CaminoSantiagoXacobeo
 */
public record CaminoSantiagoXacobeoEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public CaminoSantiagoXacobeoEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
