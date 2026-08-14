package com.proyecto.v2g.domain;

import java.util.Objects;

/**
 * Modelo de dominio puro para un Vehículo Eléctrico V2G (Vehicle-to-Grid).
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record V2GBatteryNode(
        String vehicleId,
        String h3LocationCell,
        double batteryCapacityKwh,
        double currentSocPercent,
        double maxDischargeKw,
        double minDriverSocReservePercent
) {
    public V2GBatteryNode {
        Objects.requireNonNull(vehicleId, "vehicleId no puede ser nulo");
        Objects.requireNonNull(h3LocationCell, "h3LocationCell no puede ser nulo");
        if (currentSocPercent < 0 || currentSocPercent > 100) {
            throw new IllegalArgumentException("SOC debe estar entre 0% y 100%");
        }
        if (minDriverSocReservePercent < 10.0) {
            throw new IllegalArgumentException("La reserva del conductor no puede ser inferior al 10%");
        }
    }

    public double calculateAvailableDischargeKwh() {
        double availableSoc = Math.max(0.0, currentSocPercent - minDriverSocReservePercent);
        return (availableSoc / 100.0) * batteryCapacityKwh;
    }

    public V2GBatteryNode withDischarge(double energyKwh) {
        double dischargedSoc = (energyKwh / batteryCapacityKwh) * 100.0;
        double newSoc = Math.max(minDriverSocReservePercent, currentSocPercent - dischargedSoc);
        return new V2GBatteryNode(vehicleId, h3LocationCell, batteryCapacityKwh, newSoc, maxDischargeKw, minDriverSocReservePercent);
    }
}
