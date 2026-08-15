package com.corp.ecosystem.subsurface.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: TunnelSectionGeoTwin (Geotecnia e Infraestructuras Subterráneas / EnKF).
 * <p>
 * Modela la estabilidad estructural de túneles, galerías mineras y redes de metro integrando
 * lecturas de fibra óptica (convergencia mm), piezómetros (presión intersticial) y asimilación EnKF.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference ITA-AITES Guidelines for the Design of Tunnels; Terzaghi Geotechnical Principles
 */
public record TunnelSectionGeoTwin(
        TunnelSectionId id,
        String tenantId,
        String infrastructureName,
        double chainageKm,
        GeotechnicalThresholds thresholds,
        CurrentSensorTelemetry currentTelemetry,
        StructuralHealthStatus healthStatus,
        Instant lastEvaluatedAt
) implements Serializable {

    public record TunnelSectionId(String value) {
        public TunnelSectionId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("TunnelSectionId no puede estar vacío");
        }
    }

    public record GeotechnicalThresholds(
            double maxConvergenceMm,
            double maxPiezometricPressureKpa,
            double maxStrainMicrostrain
    ) {}

    public record CurrentSensorTelemetry(
            double measuredConvergenceMm,
            double piezometricPressureKpa,
            double fiberOpticStrainMicrostrain,
            long timestampEpochMs
    ) {}

    public enum StructuralHealthStatus {
        STABLE_NORMAL, WARNING_DEFORMATION_RATE_HIGH, CRITICAL_GEOTECHNICAL_ALERT
    }

    public TunnelSectionGeoTwin recordSensorReadings(double convergenceMm, double pressureKpa, double strain) {
        CurrentSensorTelemetry telemetry = new CurrentSensorTelemetry(
                convergenceMm, pressureKpa, strain, System.currentTimeMillis()
        );

        StructuralHealthStatus status = StructuralHealthStatus.STABLE_NORMAL;
        if (convergenceMm >= thresholds.maxConvergenceMm() || pressureKpa >= thresholds.maxPiezometricPressureKpa()) {
            status = StructuralHealthStatus.CRITICAL_GEOTECHNICAL_ALERT;
        } else if (convergenceMm >= thresholds.maxConvergenceMm() * 0.75) {
            status = StructuralHealthStatus.WARNING_DEFORMATION_RATE_HIGH;
        }

        return new TunnelSectionGeoTwin(
                this.id,
                this.tenantId,
                this.infrastructureName,
                this.chainageKm,
                this.thresholds,
                telemetry,
                status,
                Instant.now()
        );
    }
}
