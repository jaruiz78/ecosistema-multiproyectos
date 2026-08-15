package com.corp.proyectodefensa.domain;

public record ProyectoDefensaPolicy(String policyId, boolean isActive) {
    public ProyectoDefensaPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
