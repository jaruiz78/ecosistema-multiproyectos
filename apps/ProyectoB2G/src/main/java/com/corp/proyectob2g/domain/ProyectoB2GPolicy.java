package com.corp.proyectob2g.domain;

public record ProyectoB2GPolicy(String policyId, boolean isActive) {
    public ProyectoB2GPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
