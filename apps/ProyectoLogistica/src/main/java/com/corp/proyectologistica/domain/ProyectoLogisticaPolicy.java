package com.corp.proyectologistica.domain;

public record ProyectoLogisticaPolicy(String policyId, boolean isActive) {
    public ProyectoLogisticaPolicy {
        if (policyId == null || policyId.isBlank()) throw new IllegalArgumentException("Invalid Policy ID");
    }
}
