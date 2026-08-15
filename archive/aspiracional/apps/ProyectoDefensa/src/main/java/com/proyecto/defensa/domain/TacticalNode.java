package com.proyecto.defensa.domain;

/**
 * Modelo de dominio puro para un nodo de malla táctica air-gapped en ProyectoDefensa.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record TacticalNode(
        String nodeId,
        String h3TacticalZone,
        boolean airGappedActive,
        long lastHeartbeatEpochMs
) {
    public TacticalNode {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("nodeId no puede ser nulo o vacío");
        }
    }

    public TacticalNode withHeartbeat(long nowEpochMs) {
        return new TacticalNode(nodeId, h3TacticalZone, airGappedActive, nowEpochMs);
    }
}
