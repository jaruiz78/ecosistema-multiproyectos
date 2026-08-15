package com.corp.agroenergyvpp.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: AgroEnergyVPP
 */
public record AgroEnergyVPPEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public AgroEnergyVPPEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
