package com.corp.proyectoagua.domain;

public record ProyectoAguaPolicy(String policyId, boolean isActive) {
    public ProyectoAguaPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
