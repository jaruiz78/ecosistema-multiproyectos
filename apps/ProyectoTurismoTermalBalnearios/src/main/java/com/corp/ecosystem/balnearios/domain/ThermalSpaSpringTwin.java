package com.corp.ecosystem.balnearios.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: ThermalSpaSpringTwin (Termalismo Histórico, Calidad Minero-Medicinal y Aforo).
 */
public record ThermalSpaSpringTwin(
        SpaSpringId id,
        String tenantId,
        String spaName,
        String autonomousCommunity,
        MineralWaterMetrics metrics,
        SpaOperationalStatus status,
        Instant lastInspectedAt
) implements Serializable {

    public record SpaSpringId(String value) {
        public SpaSpringId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("SpaSpringId no puede estar vacío");
        }
    }

    public record MineralWaterMetrics(
            double waterTemperatureCelsius,
            double mineralizationDryResidueMgL,
            double flowRateLitersPerSec,
            int currentBathersOccupancy,
            int maxSafePoolCapacity
    ) {
        public boolean isCapacityExceeded() {
            return currentBathersOccupancy > maxSafePoolCapacity;
        }

        public boolean isTemperatureOptimal() {
            return waterTemperatureCelsius >= 34.0 && waterTemperatureCelsius <= 42.0;
        }
    }

    public enum SpaOperationalStatus {
        BALNEOTHERAPY_OPTIMAL, HIGH_OCCUPANCY_RESERVED, OVERFLOW_CAPACITY_LIMIT
    }

    public static ThermalSpaSpringTwin inspectSpring(
            SpaSpringId id,
            String tenantId,
            String name,
            String community,
            MineralWaterMetrics metrics
    ) {
        SpaOperationalStatus status = metrics.isCapacityExceeded() ?
                SpaOperationalStatus.OVERFLOW_CAPACITY_LIMIT :
                (metrics.currentBathersOccupancy() >= (metrics.maxSafePoolCapacity() * 0.85) ?
                        SpaOperationalStatus.HIGH_OCCUPANCY_RESERVED :
                        SpaOperationalStatus.BALNEOTHERAPY_OPTIMAL);

        return new ThermalSpaSpringTwin(id, tenantId, name, community, metrics, status, Instant.now());
    }
}
