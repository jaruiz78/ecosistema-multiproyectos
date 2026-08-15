package com.corp.ecosystem.smartlighting.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: StreetLightingSegmentCluster (Alumbrado Público Inteligente y Recarga V2G).
 * <p>
 * Regula la intensidad lumínica LED según flujo peatonal/vehicular detectado por visión en Edge (LiteRT)
 * y coordina la carga bidireccional Vehicle-to-Grid (V2G) integrada en las farolas urbanas.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference ISO 50001 Energy Management; ISO 15118 Vehicle-to-Grid Communication Interface
 */
public record StreetLightingSegmentCluster(
        SegmentId id,
        String tenantId,
        long h3IndexRes8,
        int totalLuminaireCount,
        LightingOperatingState lightingState,
        V2gChargingHubState v2gState,
        EnergyOptimizationDecision lastDecision,
        Instant lastAdjustedAt
) implements Serializable {

    public record SegmentId(String value) {
        public SegmentId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("SegmentId no puede estar vacío");
        }
    }

    public record LightingOperatingState(
            double dimmingLevelPercentage,
            int detectedPedestriansPerMinute,
            int detectedVehiclesPerMinute,
            double ambientLuxLevel
    ) {}

    public record V2gChargingHubState(
            int connectedEvsCount,
            double aggregatedEvBatteryCapacityKwh,
            double netV2gPowerKw // Positivo: descarga a red (V2G), Negativo: carga (G2V)
    ) {}

    public record EnergyOptimizationDecision(
            double targetDimmingLevelPct,
            double energySavingsPctVsNominal,
            double netGridInjectionKw
    ) {}

    public StreetLightingSegmentCluster adjustLightingAndV2G(
            int pedestrians,
            int vehicles,
            double ambientLux,
            int connectedEvs,
            double gridStressTariffEur
    ) {
        double dimming = 20.0; // Línea base 20%
        if (ambientLux < 10.0) {
            if (pedestrians > 10 || vehicles > 15) {
                dimming = 100.0; // Máxima iluminación por seguridad
            } else if (pedestrians > 2 || vehicles > 3) {
                dimming = 60.0;
            }
        }

        double energySavings = 100.0 - dimming;

        // Decisión V2G: Si la tarifa es alta (>0.25 EUR/kWh), inyectar energía a red desde VE conectados
        double v2gPower = (gridStressTariffEur > 0.25 && connectedEvs > 0) ? (connectedEvs * 11.0) : -(connectedEvs * 7.4);

        LightingOperatingState nextLight = new LightingOperatingState(dimming, pedestrians, vehicles, ambientLux);
        V2gChargingHubState nextV2g = new V2gChargingHubState(connectedEvs, connectedEvs * 60.0, v2gPower);
        EnergyOptimizationDecision decision = new EnergyOptimizationDecision(dimming, energySavings, Math.max(0.0, v2gPower));

        return new StreetLightingSegmentCluster(
                this.id,
                this.tenantId,
                this.h3IndexRes8,
                this.totalLuminaireCount,
                nextLight,
                nextV2g,
                decision,
                Instant.now()
        );
    }
}
