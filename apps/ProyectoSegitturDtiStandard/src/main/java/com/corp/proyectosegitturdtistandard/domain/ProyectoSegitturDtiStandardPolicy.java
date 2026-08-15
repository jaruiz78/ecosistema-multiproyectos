package com.corp.proyectosegitturdtistandard.domain;

public record ProyectoSegitturDtiStandardPolicy(String policyId, boolean isActive) {
    public ProyectoSegitturDtiStandardPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
