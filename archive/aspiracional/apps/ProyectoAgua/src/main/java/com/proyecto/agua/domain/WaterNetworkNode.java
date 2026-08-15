package com.proyecto.agua.domain;

/**
 * Modelo de dominio puro (Java 25 Record) para un nodo de red hidráulica urbana/agrícola.
 * Zero-Infrastructure dependencies.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record WaterNetworkNode(
        String nodeId,
        String h3SpatialCell,
        double pressureBar,
        double flowRateLitersPerSec,
        boolean anomalyDetected
) {
    public WaterNetworkNode {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("nodeId no puede ser nulo o vacío");
        }
        if (pressureBar < 0) {
            throw new IllegalArgumentException("La presión en bar no puede ser negativa");
        }
    }

    public WaterNetworkNode withPressureUpdate(double newPressureBar, boolean newAnomalyState) {
        return new WaterNetworkNode(nodeId, h3SpatialCell, newPressureBar, flowRateLitersPerSec, newAnomalyState);
    }
}
