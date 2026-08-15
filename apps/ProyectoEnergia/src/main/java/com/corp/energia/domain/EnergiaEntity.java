package com.corp.energia.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: Energia
 */
public record EnergiaEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double voltage; double current; // Navier-Stokes / OPF Math
) {
    public EnergiaEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
