package com.corp.ecosystem.playas.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: SmartBeachTwin (Playas Inteligentes, Capacidad de Carga y Calidad de Aguas).
 */
public record SmartBeachTwin(
        BeachId id,
        String tenantId,
        String beachName,
        long h3IndexRes8,
        BeachCapacityMetrics capacity,
        WaterQualityMetrics waterQuality,
        BeachFlagStatus flagStatus,
        Instant lastMonitoredAt
) implements Serializable {

    public record BeachId(String value) {
        public BeachId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("BeachId no puede estar vacío");
        }
    }

    public record BeachCapacityMetrics(
            int currentBathersCount,
            int maxSafeCapacityCount,
            double occupancyPercentage
    ) {}

    public record WaterQualityMetrics(
            double escherichiaColiCfuPer100ml,
            double enterococciCfuPer100ml,
            double seaTemperatureCelsius,
            boolean isBlueFlagCertified
    ) {
        public boolean isBathingWaterExcellent() {
            return escherichiaColiCfuPer100ml <= 250.0 && enterococciCfuPer100ml <= 100.0;
        }
    }

    public enum BeachFlagStatus {
        GREEN_FLAG_OPTIMAL, YELLOW_FLAG_HIGH_OCCUPANCY, RED_FLAG_CONTAMINATION_OR_OVERCROWD
    }

    public static SmartBeachTwin updateBeachState(
            BeachId id,
            String tenantId,
            String name,
            long h3Index,
            int bathers,
            int maxCap,
            WaterQualityMetrics water
    ) {
        double occ = (double) bathers / maxCap * 100.0;
        BeachCapacityMetrics cap = new BeachCapacityMetrics(bathers, maxCap, occ);

        BeachFlagStatus status = BeachFlagStatus.GREEN_FLAG_OPTIMAL;
        if (!water.isBathingWaterExcellent() || occ >= 100.0) {
            status = BeachFlagStatus.RED_FLAG_CONTAMINATION_OR_OVERCROWD;
        } else if (occ >= 80.0) {
            status = BeachFlagStatus.YELLOW_FLAG_HIGH_OCCUPANCY;
        }

        return new SmartBeachTwin(id, tenantId, name, h3Index, cap, water, status, Instant.now());
    }
}
