package com.proyecto.vpp.domain;

/**
 * Modelo de dominio puro para un Recurso de Energía Distribuido (DER / Batería VPP).
 */
public record DistributedEnergyResource(
        String derId,
        String h3LocationCell,
        double capacityKwh,
        double currentSocPercent,
        double maxDischargeKw
) {
    public DistributedEnergyResource {
        if (derId == null || derId.isBlank()) {
            throw new IllegalArgumentException("derId no puede ser nulo o vacío");
        }
        if (currentSocPercent < 0 || currentSocPercent > 100) {
            throw new IllegalArgumentException("El SOC debe estar entre 0 y 100%");
        }
    }

    public DistributedEnergyResource withDischarge(double dischargedKwh) {
        double newSoc = Math.max(0.0, currentSocPercent - (dischargedKwh / capacityKwh) * 100.0);
        return new DistributedEnergyResource(derId, h3LocationCell, capacityKwh, newSoc, maxDischargeKw);
    }
}
