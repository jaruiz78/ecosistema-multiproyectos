package com.corp.droneairspaceuspace.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: DroneAirspaceUSpace
 */
public record DroneAirspaceUSpaceEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double altimeter; String h3Cell3D; // Collision Math
) {
    public DroneAirspaceUSpaceEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
