package com.corp.proyectotokenrwa.domain;

public record ProyectoTokenRWAPolicy(String policyId, boolean isActive) {
    public ProyectoTokenRWAPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
