package com.corp.subsurfacegeotwin.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: SubSurfaceGeoTwin
 */
public record SubSurfaceGeoTwinEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public SubSurfaceGeoTwinEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
