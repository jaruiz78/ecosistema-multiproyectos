package com.corp.redparadorestwin.domain;

/**
 * [AUTO-HEALED] Entidad de dominio rica inyectada tras fallo en Semantic Swarm.
 * Industry: RedParadoresTwin
 */
public record RedParadoresTwinEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double specializedMetric, String domainData
) {
    public RedParadoresTwinEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
