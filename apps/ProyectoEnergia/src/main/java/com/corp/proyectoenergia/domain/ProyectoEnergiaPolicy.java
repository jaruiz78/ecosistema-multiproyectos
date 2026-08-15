package com.corp.proyectoenergia.domain;

public record ProyectoEnergiaPolicy(String policyId, boolean isActive) {
    public ProyectoEnergiaPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
