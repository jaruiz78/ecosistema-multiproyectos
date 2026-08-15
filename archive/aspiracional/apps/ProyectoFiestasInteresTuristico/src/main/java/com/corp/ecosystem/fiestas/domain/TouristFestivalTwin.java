package com.corp.ecosystem.fiestas.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: TouristFestivalTwin (Fiestas de Interés Turístico Nacional/Internacional, Aforos y Seguridad).
 */
public record TouristFestivalTwin(
        FestivalId id,
        String tenantId,
        String festivalName,
        String declaredCategory,
        FestivalSafetyMetrics metrics,
        FestivalSecurityLevel securityLevel,
        Instant scheduledStartDate
) implements Serializable {

    public record FestivalId(String value) {
        public FestivalId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("FestivalId no puede estar vacío");
        }
    }

    public record FestivalSafetyMetrics(
            int estimatedCongregationCount,
            int maxSecurityPerimeterCapacity,
            int emergencyEvacuationCorridorsAvailable,
            boolean isEmergencyRouteClear
    ) {
        public boolean isOvercrowdedRisk() {
            return estimatedCongregationCount > maxSecurityPerimeterCapacity || !isEmergencyRouteClear;
        }
    }

    public enum FestivalSecurityLevel {
        LEVEL_GREEN_CONTROLLED, LEVEL_AMBER_RESTRICT_ACCESS, LEVEL_RED_EMERGENCY_DISPERSAL
    }

    public static TouristFestivalTwin planFestival(
            FestivalId id,
            String tenantId,
            String name,
            String category,
            FestivalSafetyMetrics metrics,
            Instant date
    ) {
        FestivalSecurityLevel level = metrics.isOvercrowdedRisk() ?
                FestivalSecurityLevel.LEVEL_RED_EMERGENCY_DISPERSAL :
                (metrics.estimatedCongregationCount() >= (metrics.maxSecurityPerimeterCapacity() * 0.85) ?
                        FestivalSecurityLevel.LEVEL_AMBER_RESTRICT_ACCESS :
                        FestivalSecurityLevel.LEVEL_GREEN_CONTROLLED);

        return new TouristFestivalTwin(id, tenantId, name, category, metrics, level, date);
    }
}
