package com.corp.corealertaggregator.domain;

/**
 * Entidad de dominio puro para Corealertaggregator.
 * Cumple política Zero-Mockito. Asignaciones de memoria deterministas.
 */
public record CorealertaggregatorEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double metricValue
) {
    public CorealertaggregatorEntity {
        if (metricValue < 0) throw new IllegalArgumentException("Metric cannot be negative");
    }
    
    public CorealertaggregatorEntity updateState(String newState, double newMetric) {
        return new CorealertaggregatorEntity(this.id, newState, this.timestamp, newMetric);
    }
}
