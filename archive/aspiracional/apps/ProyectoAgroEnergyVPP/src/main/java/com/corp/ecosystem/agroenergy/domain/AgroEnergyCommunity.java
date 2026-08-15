package com.corp.ecosystem.agroenergy.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: AgroEnergyCommunity (Comunidades Energéticas Rurales & VPP).
 * <p>
 * Optimiza el arbitraje horario de energía fotovoltaica generada en comunidades de regantes,
 * coordinando el consumo de bombeos hídricos con el almacenamiento en baterías DER y la inyección a red.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference Boyd et al. (Convex Optimization); IEEE Trans. on Power Systems (Microgrid OPF)
 */
public record AgroEnergyCommunity(
        CommunityId id,
        String tenantId,
        String communityName,
        SolarParkSpecs solarPark,
        BatteryStorageSpecs batteryStorage,
        List<PumpStationLoad> pumpLoads,
        CommunityEnergyState state,
        Instant lastDispatchedAt
) implements Serializable {

    public record CommunityId(String value) {
        public CommunityId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("CommunityId no puede estar vacío");
        }
    }

    public record SolarParkSpecs(
            double peakCapacityKw,
            double currentGenerationKw,
            double efficiencyFactor
    ) {}

    public record BatteryStorageSpecs(
            double totalCapacityKwh,
            double stateOfChargePct,
            double maxChargeDischargeKw
    ) {
        public double availableDischargeEnergyKwh() {
            return Math.max(0.0, (stateOfChargePct - 15.0) / 100.0 * totalCapacityKwh);
        }
    }

    public record PumpStationLoad(
            String pumpStationId,
            double nominalPowerKw,
            boolean isRunning,
            double priorityWeight
    ) {}

    public record DispatchInstruction(
            double solarSelfConsumptionKw,
            double batteryChargeDischargeKw, // >0 descarga, <0 carga
            double gridInjectionKw,
            double gridImportKw,
            double estimatedHourlySavingsEur
    ) {}

    public enum CommunityEnergyState {
        AUTONOMOUS_SOLAR, BATTERY_DISCHARGING, GRID_IMPORT_BACKUP, SURPLUS_INJECTION
    }

    public DispatchInstruction computeOptimalDispatch(double spotPriceEurPerMwh) {
        double currentGen = solarPark.currentGenerationKw();
        double totalPumpDemand = pumpLoads.stream()
                .filter(PumpStationLoad::isRunning)
                .mapToDouble(PumpStationLoad::nominalPowerKw)
                .sum();

        double netBalance = currentGen - totalPumpDemand;
        double solarUsed = Math.min(currentGen, totalPumpDemand);
        double batteryKw = 0.0;
        double gridInjection = 0.0;
        double gridImport = 0.0;

        if (netBalance >= 0.0) {
            // Excedente solar: cargar batería o inyectar a red
            double surplus = netBalance;
            if (batteryStorage.stateOfChargePct() < 95.0) {
                batteryKw = -Math.min(surplus, batteryStorage.maxChargeDischargeKw()); // Carga
                gridInjection = Math.max(0.0, surplus - Math.abs(batteryKw));
            } else {
                gridInjection = surplus;
            }
        } else {
            // Déficit solar: descargar batería o importar de red
            double deficit = Math.abs(netBalance);
            if (batteryStorage.availableDischargeEnergyKwh() > 1.0) {
                batteryKw = Math.min(deficit, batteryStorage.maxChargeDischargeKw()); // Descarga
                gridImport = Math.max(0.0, deficit - batteryKw);
            } else {
                gridImport = deficit;
            }
        }

        double spotEurPerKwh = spotPriceEurPerMwh / 1000.0;
        double savings = (solarUsed + Math.max(0.0, batteryKw)) * spotEurPerKwh + (gridInjection * spotEurPerKwh * 0.9);

        return new DispatchInstruction(solarUsed, batteryKw, gridInjection, gridImport, savings);
    }
}
