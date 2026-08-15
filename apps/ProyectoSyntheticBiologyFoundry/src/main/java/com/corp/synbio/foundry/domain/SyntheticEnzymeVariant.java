package com.corp.synbio.foundry.domain;

/**
 * Representa una variante enzimática sintética optimizada para fijación acelerada de CO2.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record SyntheticEnzymeVariant(
        String variantId,
        String enzymeFamily,
        String aminoAcidSequenceHash,
        double catalyticRateKcatKm,
        double thermalStabilityCelsius,
        double co2FixationRateGramsPerHour
) {
    public SyntheticEnzymeVariant {
        if (variantId == null || variantId.isBlank()) {
            throw new IllegalArgumentException("variantId no puede estar vacío");
        }
        if (catalyticRateKcatKm <= 0.0) {
            throw new IllegalArgumentException("La tasa catalítica debe ser positiva");
        }
        if (thermalStabilityCelsius < 20.0 || thermalStabilityCelsius > 120.0) {
            throw new IllegalArgumentException("Estabilidad térmica debe estar entre 20°C y 120°C");
        }
    }

    public boolean isCommerciallyViable() {
        // Viabilidad comercial: fijación >= 5.0 gCO2/h y estabilidad térmica >= 45°C
        return co2FixationRateGramsPerHour >= 5.0 && thermalStabilityCelsius >= 45.0;
    }
}
