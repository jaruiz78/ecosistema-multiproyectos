package com.corp.ecosystem.heritage3d.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: MonumentStructuralTwin (Gemelo Digital 3D LiDAR y Preservación de Monumentos Históricos).
 */
public record MonumentStructuralTwin(
        MonumentId id,
        String tenantId,
        String monumentName,
        long pointCloudCount,
        StructuralHealthMetrics metrics,
        ConservationUrgency urgencyLevel,
        Instant scannedAt
) implements Serializable {

    public record MonumentId(String value) {
        public MonumentId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("MonumentId no puede estar vacío");
        }
    }

    public record StructuralHealthMetrics(
            double crackDisplacementMm,
            double moistureContentPercentage,
            double microVibrationFrequencyHz,
            double maxAllowedDisplacementMm
    ) {
        public boolean isDisplacementCritical() {
            return crackDisplacementMm > maxAllowedDisplacementMm || moistureContentPercentage > 25.0;
        }
    }

    public enum ConservationUrgency {
        STABLE_NORMAL, MONITORING_RECOMMENDED, URGENT_RESTORATION_REQUIRED
    }

    public static MonumentStructuralTwin analyzeScan(
            MonumentId id,
            String tenantId,
            String name,
            long points,
            StructuralHealthMetrics metrics
    ) {
        ConservationUrgency urgency = metrics.isDisplacementCritical() ?
                ConservationUrgency.URGENT_RESTORATION_REQUIRED :
                (metrics.crackDisplacementMm() >= (metrics.maxAllowedDisplacementMm() * 0.75) ?
                        ConservationUrgency.MONITORING_RECOMMENDED :
                        ConservationUrgency.STABLE_NORMAL);

        return new MonumentStructuralTwin(id, tenantId, name, points, metrics, urgency, Instant.now());
    }
}
