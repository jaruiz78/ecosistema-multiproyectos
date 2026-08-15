package com.corp.ecosystem.cascohistorico.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: OldTownHeritageZoneTwin (Cascos Históricos UNESCO, Capacidad de Carga y Dispersión Acústica).
 */
public record OldTownHeritageZoneTwin(
        ZoneId id,
        String tenantId,
        String oldTownQuarterName,
        long h3IndexRes8,
        HeritageFlowMetrics metrics,
        QuarterCrowdStatus status,
        Instant lastObservedAt
) implements Serializable {

    public record ZoneId(String value) {
        public ZoneId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ZoneId no puede estar vacío");
        }
    }

    public record HeritageFlowMetrics(
            int currentPedestrianDensityCount,
            int maxHeritageCarryingCapacity,
            double ambientNoiseDecibels,
            double residentQualityOfLifeIndex
    ) {
        public boolean isHeritageOverburdened() {
            return currentPedestrianDensityCount > maxHeritageCarryingCapacity || ambientNoiseDecibels > 70.0;
        }
    }

    public enum QuarterCrowdStatus {
        OPTIMAL_FLUID_WALK, DENSE_MODERATE_ALERT, OVERBURDENED_DISPERSION_TRIGGERED
    }

    public static OldTownHeritageZoneTwin evaluateQuarter(
            ZoneId id,
            String tenantId,
            String quarterName,
            long h3Index,
            HeritageFlowMetrics metrics
    ) {
        QuarterCrowdStatus status = metrics.isHeritageOverburdened() ?
                QuarterCrowdStatus.OVERBURDENED_DISPERSION_TRIGGERED :
                (metrics.currentPedestrianDensityCount() >= (metrics.maxHeritageCarryingCapacity() * 0.8) ?
                        QuarterCrowdStatus.DENSE_MODERATE_ALERT :
                        QuarterCrowdStatus.OPTIMAL_FLUID_WALK);

        return new OldTownHeritageZoneTwin(id, tenantId, quarterName, h3Index, metrics, status, Instant.now());
    }
}
