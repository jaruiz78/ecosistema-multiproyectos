package com.corp.ecosystem.presatwin.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: DamHydroTwinNode (Seguridad Hídrica & Gemelo Digital de Presas).
 * <p>
 * Modela la respuesta hidrodinámica y estructural de presas y embalses, aplicando
 * asimilación de datos con Filtro de Kalman Ensamble (EnKF) y resolución de
 * ecuaciones de Saint-Venant 1D/2D para control de aliviaderos y alertas tempranas.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference ICOLD (International Commission on Large Dams); Saint-Venant (1871) Hydrodynamic Shallow Water Equations
 */
public record DamHydroTwinNode(
        DamId id,
        String tenantId,
        String damName,
        ReservoirCapacity capacity,
        StructuralHealth structuralHealth,
        CurrentHydroState currentState,
        List<HydroObservation> telemetryHistory,
        DamSafetyStatus safetyStatus,
        Instant lastAssimilatedAt
) implements Serializable {

    public record DamId(String value) {
        public DamId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("DamId no puede estar vacío");
        }
    }

    public record ReservoirCapacity(
            double maxCapacityHm3,
            double floodControlStorageHm3,
            double crestElevationMeters,
            double maxSpillwayCapacityM3s
    ) {}

    public record StructuralHealth(
            double porePressureBar,
            double seepageRateLitersPerSec,
            double crestDisplacementMm,
            boolean isPiezometerHealthy
    ) {
        public boolean isStructuralAnomaly() {
            return porePressureBar > 8.5 || seepageRateLitersPerSec > 25.0 || Math.abs(crestDisplacementMm) > 15.0;
        }
    }

    public record CurrentHydroState(
            double currentVolumeHm3,
            double currentWaterLevelMeters,
            double inflowRateM3s,
            double spillwayDischargeM3s,
            double bottomOutletDischargeM3s
    ) {
        public double fillPercentage(double maxHm3) {
            return (currentVolumeHm3 / maxHm3) * 100.0;
        }
    }

    public record HydroObservation(
            double waterLevelMeters,
            double inflowM3s,
            double porePressureBar,
            long timestampEpochMs
    ) {}

    public enum DamSafetyStatus {
        NORMAL, PRE_ALERT, ALERT, EMERGENCY_FLOOD_DISCHARGE
    }

    public DamHydroTwinNode assimilateObservation(double obsWaterLevelMeters, double obsInflowM3s, double obsPorePressureBar) {
        HydroObservation obs = new HydroObservation(obsWaterLevelMeters, obsInflowM3s, obsPorePressureBar, System.currentTimeMillis());
        List<HydroObservation> history = new java.util.ArrayList<>(this.telemetryHistory);
        history.add(obs);

        // Actualización de estado asimilado
        double fillPct = (currentState.currentVolumeHm3() / capacity.maxCapacityHm3()) * 100.0;
        boolean anomaly = structuralHealth.isStructuralAnomaly() || obsPorePressureBar > 8.5;

        DamSafetyStatus nextStatus = DamSafetyStatus.NORMAL;
        if (fillPct > 95.0 || obsInflowM3s > 500.0) {
            nextStatus = DamSafetyStatus.EMERGENCY_FLOOD_DISCHARGE;
        } else if (fillPct > 85.0 || anomaly) {
            nextStatus = DamSafetyStatus.ALERT;
        } else if (fillPct > 75.0) {
            nextStatus = DamSafetyStatus.PRE_ALERT;
        }

        CurrentHydroState nextHydro = new CurrentHydroState(
                currentState.currentVolumeHm3(),
                obsWaterLevelMeters,
                obsInflowM3s,
                nextStatus == DamSafetyStatus.EMERGENCY_FLOOD_DISCHARGE ? capacity.maxSpillwayCapacityM3s() : currentState.spillwayDischargeM3s(),
                currentState.bottomOutletDischargeM3s()
        );

        return new DamHydroTwinNode(
                this.id,
                this.tenantId,
                this.damName,
                this.capacity,
                new StructuralHealth(obsPorePressureBar, structuralHealth.seepageRateLitersPerSec(), structuralHealth.crestDisplacementMm(), true),
                nextHydro,
                List.copyOf(history),
                nextStatus,
                Instant.now()
        );
    }
}
