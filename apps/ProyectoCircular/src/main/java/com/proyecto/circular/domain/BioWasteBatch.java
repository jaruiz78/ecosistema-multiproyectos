package com.proyecto.circular.domain;

/**
 * Modelo de dominio puro para un Lote de Bio-Residuos con trazabilidad de economía circular.
 */
public record BioWasteBatch(
        String batchId,
        String wasteCategory,
        double weightKg,
        double recycledRatioPercent,
        boolean certifiedCompliant
) {
    public BioWasteBatch {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("batchId no puede ser nulo o vacío");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("El peso del lote debe ser mayor a 0 kg");
        }
    }

    public BioWasteBatch withCertification(boolean compliant) {
        return new BioWasteBatch(batchId, wasteCategory, weightKg, recycledRatioPercent, compliant);
    }
}
