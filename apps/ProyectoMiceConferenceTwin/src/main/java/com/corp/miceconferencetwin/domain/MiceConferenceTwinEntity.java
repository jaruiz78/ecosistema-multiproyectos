package com.corp.miceconferencetwin.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: MiceConferenceTwin
 */
public record MiceConferenceTwinEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public MiceConferenceTwinEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
