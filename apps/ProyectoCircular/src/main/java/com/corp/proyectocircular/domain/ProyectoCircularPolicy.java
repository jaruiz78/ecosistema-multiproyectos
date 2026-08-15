package com.corp.proyectocircular.domain;

public record ProyectoCircularPolicy(String policyId, boolean isActive) {
    public ProyectoCircularPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
