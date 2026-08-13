package com.proyecto.defensa.domain;

/**
 * Modelo de dominio puro para un nodo de malla táctica air-gapped en ProyectoDefensa.
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
