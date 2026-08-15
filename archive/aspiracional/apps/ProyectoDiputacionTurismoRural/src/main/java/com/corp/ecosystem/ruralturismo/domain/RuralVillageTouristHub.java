package com.corp.ecosystem.ruralturismo.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: RuralVillageTouristHub (Dinamización Turística Rural y Reto Demográfico Provincial).
 */
public record RuralVillageTouristHub(
        HubId id,
        String tenantId,
        String villageName,
        String provinceName,
        int registeredInhabitants,
        CapacityMetrics capacity,
        RevitalizationStatus status,
        Instant lastEvaluatedAt
) implements Serializable {

    public record HubId(String value) {
        public HubId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("HubId no puede estar vacío");
        }
    }

    public record CapacityMetrics(
            int ruralAccommodationsCount,
            int activeHikingTrailsCount,
            double seasonalOccupancyRatePct,
            double economicImpactEurPerYear
    ) {}

    public enum RevitalizationStatus {
        HIGH_TRACTION_ECOTURISMO, STABLE_RURAL_HUB, AT_RISK_DEPPOPULATION
    }

    public static RuralVillageTouristHub evaluateVillage(
            HubId id,
            String tenantId,
            String name,
            String province,
            int inhabitants,
            CapacityMetrics metrics
    ) {
        RevitalizationStatus status = (metrics.seasonalOccupancyRatePct() >= 65.0 && metrics.ruralAccommodationsCount() >= 5) ?
                RevitalizationStatus.HIGH_TRACTION_ECOTURISMO :
                (inhabitants < 100 && metrics.ruralAccommodationsCount() <= 1 ?
                        RevitalizationStatus.AT_RISK_DEPPOPULATION :
                        RevitalizationStatus.STABLE_RURAL_HUB);

        return new RuralVillageTouristHub(
                id,
                tenantId,
                name,
                province,
                inhabitants,
                metrics,
                status,
                Instant.now()
        );
    }
}
