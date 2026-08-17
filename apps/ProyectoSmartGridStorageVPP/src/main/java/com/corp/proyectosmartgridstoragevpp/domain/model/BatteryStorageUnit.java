package com.corp.proyectosmartgridstoragevpp.domain.model;

import java.io.Serializable;

/**
 * Agregado raíz que representa una batería estacionaria BESS (Battery Energy Storage System)
 * con estado de carga (SoC) y degradación de ciclo en $O(1)$.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 */
public record BatteryStorageUnit(
        String batteryId,
        double capacityKwh,
        double currentSocPct, // 0.0 a 100.0
        double maxCrateChargeKw,
        double maxCrateDischargeKw,
        double stateOfHealthPct,
        BatteryState state
) implements Serializable {

    public enum BatteryState {
        IDLE,
        CHARGING,
        DISCHARGING,
        MAINTENANCE
    }

    public static BatteryStorageUnit create(String id, double capacityKwh, double maxPowerKw) {
        return new BatteryStorageUnit(id, capacityKwh, 50.0, maxPowerKw, maxPowerKw, 100.0, BatteryState.IDLE);
    }

    public BatteryStorageUnit charge(double energyKwh, double powerKw) {
        if (state == BatteryState.MAINTENANCE) {
            throw new IllegalStateException("Batería en mantenimiento");
        }
        double addedSoc = (energyKwh / capacityKwh) * 100.0;
        double newSoc = Math.min(100.0, currentSocPct + addedSoc);
        double degradation = (energyKwh / (capacityKwh * 3000.0)) * 100.0;
        double newSoh = Math.max(0.0, stateOfHealthPct - degradation);

        return new BatteryStorageUnit(batteryId, capacityKwh, newSoc, maxCrateChargeKw, maxCrateDischargeKw, newSoh, BatteryState.CHARGING);
    }

    public BatteryStorageUnit discharge(double energyKwh, double powerKw) {
        if (state == BatteryState.MAINTENANCE) {
            throw new IllegalStateException("Batería en mantenimiento");
        }
        double removedSoc = (energyKwh / capacityKwh) * 100.0;
        double newSoc = Math.max(0.0, currentSocPct - removedSoc);
        double degradation = (energyKwh / (capacityKwh * 3000.0)) * 100.0;
        double newSoh = Math.max(0.0, stateOfHealthPct - degradation);

        return new BatteryStorageUnit(batteryId, capacityKwh, newSoc, maxCrateChargeKw, maxCrateDischargeKw, newSoh, BatteryState.DISCHARGING);
    }
}
