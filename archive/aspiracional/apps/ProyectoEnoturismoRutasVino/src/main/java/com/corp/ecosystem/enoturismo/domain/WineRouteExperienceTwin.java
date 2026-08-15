package com.corp.ecosystem.enoturismo.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: WineRouteExperienceTwin (Rutas de Enoturismo, Denominación de Origen y Bodegas).
 */
public record WineRouteExperienceTwin(
        ExperienceId id,
        String tenantId,
        String doRegionName,
        String wineryName,
        WineryTourismMetrics metrics,
        TastingSessionStatus status,
        Instant scheduledAt
) implements Serializable {

    public record ExperienceId(String value) {
        public ExperienceId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ExperienceId no puede estar vacío");
        }
    }

    public record WineryTourismMetrics(
            int currentVisitorsCount,
            int maxTastingRoomCapacity,
            double sommelierScoreIndex,
            double localWineSalesEur
    ) {
        public boolean isCapacityAvailable() {
            return currentVisitorsCount < maxTastingRoomCapacity;
        }
    }

    public enum TastingSessionStatus {
        CONFIRMED_EXCLUSIVE_TASTING, FULLY_BOOKED_HARVEST_SEASON, RESCHEDULED_CELLAR_REST
    }

    public static WineRouteExperienceTwin scheduleExperience(
            ExperienceId id,
            String tenantId,
            String doRegion,
            String winery,
            WineryTourismMetrics metrics
    ) {
        TastingSessionStatus status = metrics.isCapacityAvailable() ?
                TastingSessionStatus.CONFIRMED_EXCLUSIVE_TASTING :
                TastingSessionStatus.FULLY_BOOKED_HARVEST_SEASON;

        return new WineRouteExperienceTwin(id, tenantId, doRegion, winery, metrics, status, Instant.now());
    }
}
