package com.corp.ecosystem.mice.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: MiceEventCongressTwin (Turismo MICE, Congresos y Ferias Internacionales).
 */
public record MiceEventCongressTwin(
        CongressId id,
        String tenantId,
        String eventName,
        String conventionCenterName,
        CongressMetrics metrics,
        EventOperationalStatus status,
        Instant scheduledDate
) implements Serializable {

    public record CongressId(String value) {
        public CongressId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("CongressId no puede estar vacío");
        }
    }

    public record CongressMetrics(
            int registeredAttendees,
            int internationalExhibitors,
            double economicImpactCityEur,
            double hotelOccupancyRatePct
    ) {}

    public enum EventOperationalStatus {
        TIER1_INTERNATIONAL_FLAGSHIP, STANDARD_CONGRESS_ACTIVE, RESCHEDULED
    }

    public static MiceEventCongressTwin registerEvent(
            CongressId id,
            String tenantId,
            String name,
            String center,
            CongressMetrics metrics,
            Instant date
    ) {
        EventOperationalStatus status = (metrics.registeredAttendees() >= 10000 && metrics.internationalExhibitors() >= 200) ?
                EventOperationalStatus.TIER1_INTERNATIONAL_FLAGSHIP :
                EventOperationalStatus.STANDARD_CONGRESS_ACTIVE;

        return new MiceEventCongressTwin(id, tenantId, name, center, metrics, status, date);
    }
}
