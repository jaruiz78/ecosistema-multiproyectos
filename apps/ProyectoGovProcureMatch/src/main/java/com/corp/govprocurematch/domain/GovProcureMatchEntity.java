package com.corp.govprocurematch.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: GovProcureMatch
 */
public record GovProcureMatchEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric; String domainData; // O(1) Tensor Math
) {
    public GovProcureMatchEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
