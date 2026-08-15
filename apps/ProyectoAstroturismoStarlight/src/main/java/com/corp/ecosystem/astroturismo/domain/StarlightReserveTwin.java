package com.corp.ecosystem.astroturismo.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: StarlightReserveTwin (Astroturismo, Calidad del Cielo Oscuro y Observación Astronómica).
 */
public record StarlightReserveTwin(
        ReserveId id,
        String tenantId,
        String reserveName,
        long h3IndexRes8,
        DarkSkyQualityMetrics metrics,
        StarlightObservationQuality observationQuality,
        Instant measuredAt
) implements Serializable {

    public record ReserveId(String value) {
        public ReserveId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ReserveId no puede estar vacío");
        }
    }

    public record DarkSkyQualityMetrics(
            double skyQualityMeterMagArcsec2, // SQM (Valores óptimos > 21.5 mag/arcsec²)
            double cloudCoveragePercentage,
            double artificialLightPollutionLumens,
            double seeingAtmosphericArcsec
    ) {
        public boolean isStarlightCertifiedDarkSky() {
            return skyQualityMeterMagArcsec2 >= 21.5 && artificialLightPollutionLumens < 50.0;
        }
    }

    public enum StarlightObservationQuality {
        PRISTINE_DARK_SKY_EXCELLENT, GOOD_ASTRONOMICAL_CONDITIONS, LIGHT_POLLUTION_IMPAIRED
    }

    public static StarlightReserveTwin evaluateSky(
            ReserveId id,
            String tenantId,
            String name,
            long h3Index,
            DarkSkyQualityMetrics metrics
    ) {
        StarlightObservationQuality quality = metrics.isStarlightCertifiedDarkSky() ?
                (metrics.cloudCoveragePercentage() < 15.0 ?
                        StarlightObservationQuality.PRISTINE_DARK_SKY_EXCELLENT :
                        StarlightObservationQuality.GOOD_ASTRONOMICAL_CONDITIONS) :
                StarlightObservationQuality.LIGHT_POLLUTION_IMPAIRED;

        return new StarlightReserveTwin(id, tenantId, name, h3Index, metrics, quality, Instant.now());
    }
}
