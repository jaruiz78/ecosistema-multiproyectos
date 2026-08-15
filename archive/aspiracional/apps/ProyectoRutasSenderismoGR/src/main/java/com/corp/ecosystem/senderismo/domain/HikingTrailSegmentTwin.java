package com.corp.ecosystem.senderismo.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: HikingTrailSegmentTwin (Senderos GR/PR, Seguridad en Montaña y Balizamiento IoT).
 */
public record HikingTrailSegmentTwin(
        TrailSegmentId id,
        String tenantId,
        String trailCodeName,
        int distanceMeters,
        int positiveElevationGainMeters,
        TrailSafetyMetrics metrics,
        TrailStatus status,
        Instant lastBeaconHeartbeatAt
) implements Serializable {

    public record TrailSegmentId(String value) {
        public TrailSegmentId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("TrailSegmentId no puede estar vacío");
        }
    }

    public record TrailSafetyMetrics(
            int activeHikersOnSegmentCount,
            double windSpeedKmh,
            double precipitationMmPerHour,
            boolean isLandslideRiskAlertActive,
            int availableEmergencySheltersCount
    ) {
        public boolean isSevereWeatherDanger() {
            return windSpeedKmh > 75.0 || precipitationMmPerHour > 30.0 || isLandslideRiskAlertActive;
        }
    }

    public enum TrailStatus {
        TRAIL_OPEN_OPTIMAL, CAUTION_WEATHER_ADVISORY, TRAIL_CLOSED_EMERGENCY
    }

    public static HikingTrailSegmentTwin evaluateSegment(
            TrailSegmentId id,
            String tenantId,
            String codeName,
            int distance,
            int elevationGain,
            TrailSafetyMetrics metrics
    ) {
        TrailStatus status = metrics.isSevereWeatherDanger() ?
                TrailStatus.TRAIL_CLOSED_EMERGENCY :
                (metrics.windSpeedKmh() > 50.0 || metrics.precipitationMmPerHour() > 15.0 ?
                        TrailStatus.CAUTION_WEATHER_ADVISORY :
                        TrailStatus.TRAIL_OPEN_OPTIMAL);

        return new HikingTrailSegmentTwin(id, tenantId, codeName, distance, elevationGain, metrics, status, Instant.now());
    }
}
