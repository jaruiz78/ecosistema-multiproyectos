package com.corp.proyectosalud.domain;

public record ProyectoSaludPolicy(String policyId, boolean isActive) {
    public ProyectoSaludPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
