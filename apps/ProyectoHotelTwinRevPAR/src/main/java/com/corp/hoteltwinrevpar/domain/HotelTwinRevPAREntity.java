package com.corp.hoteltwinrevpar.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: HotelTwinRevPAR
 */
public record HotelTwinRevPAREntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public HotelTwinRevPAREntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
