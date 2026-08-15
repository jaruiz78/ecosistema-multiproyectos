package com.proyecto.desal.domain;

import java.util.Objects;

/**
 * Modelo de dominio puro para una Planta Desalinizadora de Ósmosis Inversa.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record DesalinationPlant(
        String plantId,
        String h3LocationCell,
        double maxProductionCapacityM3PerHour,
        double specificEnergyKwhPerM3,
        double currentProductionRatePercent,
        double maxBrineSalinityGramsPerLiter
) {
    public DesalinationPlant {
        Objects.requireNonNull(plantId, "plantId no puede ser nulo");
        Objects.requireNonNull(h3LocationCell, "h3LocationCell no puede ser nulo");
        if (specificEnergyKwhPerM3 <= 0) {
            throw new IllegalArgumentException("La energía específica debe ser positiva");
        }
        if (currentProductionRatePercent < 0 || currentProductionRatePercent > 100) {
            throw new IllegalArgumentException("El régimen de producción debe estar entre 0% y 100%");
        }
    }

    public double calculatePowerDemandKw() {
        double currentM3PerHour = (currentProductionRatePercent / 100.0) * maxProductionCapacityM3PerHour;
        return currentM3PerHour * specificEnergyKwhPerM3;
    }

    public DesalinationPlant withProductionRate(double newRatePercent) {
        return new DesalinationPlant(plantId, h3LocationCell, maxProductionCapacityM3PerHour, specificEnergyKwhPerM3, Math.clamp(newRatePercent, 0.0, 100.0), maxBrineSalinityGramsPerLiter);
    }
}
