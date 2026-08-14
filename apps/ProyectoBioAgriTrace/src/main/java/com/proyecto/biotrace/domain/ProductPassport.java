package com.proyecto.biotrace.domain;

import java.util.Objects;

/**
 * Modelo de dominio puro para el Pasaporte Digital de Producto Agrario (DPP UE 2026).
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record ProductPassport(
        String batchId,
        String h3PlotCell,
        String cropType,
        double waterFootprintLitersPerKg,
        double carbonFootprintGramsCo2PerKg,
        boolean zeroChemicalResidue,
        String merkleQrDigest
) {
    public ProductPassport {
        Objects.requireNonNull(batchId, "batchId no puede ser nulo");
        Objects.requireNonNull(h3PlotCell, "h3PlotCell no puede ser nulo");
        Objects.requireNonNull(cropType, "cropType no puede ser nulo");
        if (waterFootprintLitersPerKg < 0 || carbonFootprintGramsCo2PerKg < 0) {
            throw new IllegalArgumentException("Las huellas ambiental y de agua deben ser no negativas");
        }
    }

    public boolean isBioExportCertified() {
        return zeroChemicalResidue && waterFootprintLitersPerKg <= 150.0 && carbonFootprintGramsCo2PerKg <= 120.0;
    }
}
