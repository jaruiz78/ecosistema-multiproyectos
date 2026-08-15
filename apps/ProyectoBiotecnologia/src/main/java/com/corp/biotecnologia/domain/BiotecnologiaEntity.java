package com.corp.biotecnologia.domain;

/**
 * Entidad de dominio rica inyectada por corp-cli (Agentic Mode).
 * Industry: Biotecnologia
 */
public record BiotecnologiaEntity(
    java.util.UUID id,
    String state,
    long timestamp,
    double phLevel,
    double cellCount // Petri Network Mutation
) {
    public BiotecnologiaEntity {
        if (timestamp < 0) throw new IllegalArgumentException("Invalid state");
    }
}
