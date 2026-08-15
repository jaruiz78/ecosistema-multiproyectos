package com.corp.proyectogovprocurematch.domain;

public record ProyectoGovProcureMatchPolicy(String policyId, boolean isActive) {
    public ProyectoGovProcureMatchPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
