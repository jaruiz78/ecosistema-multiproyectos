package com.proyecto.circular.domain;

/**
 * Modelo de dominio puro para un Lote de Bio-Residuos con trazabilidad de economía circular.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
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
