package com.corp.ecosystem.paradores.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: HistoricParadorTwin (Red de Paradores Nacionales de Turismo y Eficiencia en Patrimonio).
 */
public record HistoricParadorTwin(
        ParadorId id,
        String tenantId,
        String paradorName,
        String historicMonumentCategory,
        BuildingThermalProfile thermalProfile,
        ParadorOperationalStatus status,
        Instant lastEnergyEvaluatedAt
) implements Serializable {

    public record ParadorId(String value) {
        public ParadorId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ParadorId no puede estar vacío");
        }
    }

    public record BuildingThermalProfile(
            double indoorTemperatureCelsius,
            double targetComfortTemperatureCelsius,
            double geothermalHvacPowerKw,
            double historicalConservationHumidityPct
    ) {
        public boolean isHeritageConservationSafe() {
            // Humedad relativa entre 45% y 60% para preservación de retablos y maderas históricas
            return historicalConservationHumidityPct >= 45.0 && historicalConservationHumidityPct <= 60.0;
        }
    }

    public enum ParadorOperationalStatus {
        HERITAGE_ENERGY_OPTIMIZED, CLIMATE_EXCURSION_ADJUSTING, EMERGENCY_RESTORATION_LOCK
    }

    public static HistoricParadorTwin evaluateParador(
            ParadorId id,
            String tenantId,
            String name,
            String category,
            BuildingThermalProfile thermal
    ) {
        ParadorOperationalStatus status = thermal.isHeritageConservationSafe() ?
                ParadorOperationalStatus.HERITAGE_ENERGY_OPTIMIZED :
                ParadorOperationalStatus.CLIMATE_EXCURSION_ADJUSTING;

        return new HistoricParadorTwin(id, tenantId, name, category, thermal, status, Instant.now());
    }
}
