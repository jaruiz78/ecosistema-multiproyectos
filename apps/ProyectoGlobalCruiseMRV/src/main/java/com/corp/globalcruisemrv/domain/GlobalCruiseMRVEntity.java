package com.corp.globalcruisemrv.domain;

/**
 * Entidad de dominio rica inyectada por Semantic Swarm.
 * Industry: GlobalCruiseMRV
 */
public record GlobalCruiseMRVEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double knots; double ballast; // Hydrodynamics
) {
    public GlobalCruiseMRVEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
