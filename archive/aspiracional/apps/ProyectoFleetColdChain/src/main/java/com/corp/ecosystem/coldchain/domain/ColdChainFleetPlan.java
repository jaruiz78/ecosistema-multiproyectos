package com.corp.ecosystem.coldchain.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: ColdChainFleetPlan (Logística de Frío & VRPTW).
 * <p>
 * Gestiona el plan de ruta y la integridad térmica de cargamentos farmacéuticos y perecederos,
 * detectando excursiones de temperatura fuera de los límites de Good Distribution Practices (GDP).
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference WHO Technical Report Series No. 961 (Good Distribution Practices for Pharmaceutical Products)
 */
public record ColdChainFleetPlan(
        RoutePlanId id,
        String tenantId,
        String vehicleId,
        TemperatureCategory requiredCategory,
        ThermalRange thermalRange,
        List<DeliveryStop> stops,
        List<ThermalReading> telemetryLog,
        PlanState state,
        Instant departureTime
) implements Serializable {

    public record RoutePlanId(String value) {
        public RoutePlanId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("RoutePlanId no puede estar vacío");
        }
    }

    public enum TemperatureCategory {
        ULTRA_COLD_CRYO(-80.0, -60.0), // Vacunas ARN / Material Biológico
        FROZEN(-25.0, -15.0),           // Alimentos ultracongelados
        REFRIGERATED_PHARMA(2.0, 8.0),  // Medicamentos / Insulina
        CONTROLLED_ROOM_TEMP(15.0, 25.0);// Cosméticos / Químicos sensibles

        private final double minTemp;
        private final double maxTemp;

        TemperatureCategory(double minTemp, double maxTemp) {
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }

        public double getMinTemp() { return minTemp; }
        public double getMaxTemp() { return maxTemp; }
    }

    public record ThermalRange(double minAllowedCelsius, double maxAllowedCelsius) {
        public boolean isExcursion(double currentCelsius) {
            return currentCelsius < minAllowedCelsius || currentCelsius > maxAllowedCelsius;
        }
    }

    public record DeliveryStop(
            String stopId,
            long h3LocationRes8,
            int timeWindowStartSec,
            int timeWindowEndSec,
            boolean completed
    ) {}

    public record ThermalReading(
            double temperatureCelsius,
            double humidityPct,
            long timestampEpochMs,
            boolean isExcursion
    ) {}

    public enum PlanState {
        SCHEDULED, IN_TRANSIT, COMPLETED_INTACT, COMPLETED_WITH_EXCURSION, CANCELLED
    }

    public ColdChainFleetPlan recordThermalReading(double tempCelsius, double humidityPct, long epochMs) {
        boolean excursion = thermalRange.isExcursion(tempCelsius);
        ThermalReading reading = new ThermalReading(tempCelsius, humidityPct, epochMs, excursion);

        List<ThermalReading> updatedLog = new java.util.ArrayList<>(this.telemetryLog);
        updatedLog.add(reading);

        PlanState nextState = this.state;
        if (excursion && this.state == PlanState.IN_TRANSIT) {
            nextState = PlanState.COMPLETED_WITH_EXCURSION;
        }

        return new ColdChainFleetPlan(
                this.id,
                this.tenantId,
                this.vehicleId,
                this.requiredCategory,
                this.thermalRange,
                this.stops,
                List.copyOf(updatedLog),
                nextState,
                this.departureTime
        );
    }
}
