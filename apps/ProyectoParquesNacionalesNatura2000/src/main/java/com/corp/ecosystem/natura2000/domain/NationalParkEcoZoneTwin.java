package com.corp.ecosystem.natura2000.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: NationalParkEcoZoneTwin (Parques Nacionales, Red Natura 2000 y Capacidad de Carga Ecológica).
 */
public record NationalParkEcoZoneTwin(
        ZoneId id,
        String tenantId,
        String parkName,
        long h3IndexRes8,
        EcoCarryingCapacityMetrics metrics,
        EcoZoneAccessStatus status,
        Instant lastSurveyedAt
) implements Serializable {

    public record ZoneId(String value) {
        public ZoneId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ZoneId no puede estar vacío");
        }
    }

    public record EcoCarryingCapacityMetrics(
            int currentHikersInZone,
            int maxEcologicalLimitHikers,
            double nestingFaunaDisturbanceIndex,
            boolean isWildfireHighRiskPeriod
    ) {
        public boolean isEcoLimitExceeded() {
            return currentHikersInZone > maxEcologicalLimitHikers || nestingFaunaDisturbanceIndex > 0.75;
        }
    }

    public enum EcoZoneAccessStatus {
        OPEN_SUSTAINABLE_ACCESS, REGULATED_SLOT_RESTRICTION, TEMPORARY_ECO_CLOSURE
    }

    public static NationalParkEcoZoneTwin evaluateZone(
            ZoneId id,
            String tenantId,
            String name,
            long h3Index,
            EcoCarryingCapacityMetrics metrics
    ) {
        EcoZoneAccessStatus status = metrics.isEcoLimitExceeded() ?
                EcoZoneAccessStatus.TEMPORARY_ECO_CLOSURE :
                (metrics.currentHikersInZone() >= (metrics.maxEcologicalLimitHikers() * 0.8) ?
                        EcoZoneAccessStatus.REGULATED_SLOT_RESTRICTION :
                        EcoZoneAccessStatus.OPEN_SUSTAINABLE_ACCESS);

        return new NationalParkEcoZoneTwin(id, tenantId, name, h3Index, metrics, status, Instant.now());
    }
}
