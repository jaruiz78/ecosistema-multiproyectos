package com.corp.salud.domain;

/**
 * Entidad de dominio puro para Salud.
 * Cumple política Zero-Mockito. Asignaciones de memoria deterministas.
 */
public record SaludEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double metricValue
) {
    public SaludEntity {
        if (metricValue < 0) throw new IllegalArgumentException("Metric cannot be negative");
    }
    
    public SaludEntity updateState(String newState, double newMetric) {
        return new SaludEntity(this.id, newState, this.timestamp, newMetric);
    }
}
