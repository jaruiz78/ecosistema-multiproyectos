package com.corp.soilbiocarbontwin.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: SoilBioCarbonTwin
 */
public record SoilBioCarbonTwinEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public SoilBioCarbonTwinEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
