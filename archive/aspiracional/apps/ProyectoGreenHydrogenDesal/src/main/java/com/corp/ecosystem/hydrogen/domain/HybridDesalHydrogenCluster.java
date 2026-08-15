package com.corp.ecosystem.hydrogen.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: HybridDesalHydrogenCluster (Hidrógeno Verde y Desalación con MPC).
 * <p>
 * Modela el despacho óptimo conjunto de un electrolizador PEM de \(H_2\) y una planta desaladora por ósmosis inversa (SWRO)
 * alimentados por energía solar y eólica para maximizar el margen económico y la eficiencia hídrico-energética.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference EU Hydrogen Strategy (Clean Hydrogen Partnership); International Desalination Association (IDA)
 */
public record HybridDesalHydrogenCluster(
        PlantId id,
        String tenantId,
        PlantCapacities capacities,
        CurrentOperatingState state,
        MpcDispatchSetpoint currentSetpoint,
        Instant lastOptimizedAt
) implements Serializable {

    public record PlantId(String value) {
        public PlantId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("PlantId no puede estar vacío");
        }
    }

    public record PlantCapacities(
            double electrolyzerMaxMw,
            double desalMaxM3Day,
            double solarPvInstalledMw,
            double windInstalledMw
    ) {}

    public record CurrentOperatingState(
            double availableRenewablePowerMw,
            double spotElectricityPriceEurMwh,
            double currentHydrogenKgHour,
            double currentDesalWaterM3Hour
    ) {}

    public record MpcDispatchSetpoint(
            double allocatedElectrolyzerMw,
            double allocatedDesalMw,
            double hydrogenProductionKgHour,
            double desalWaterProductionM3Hour,
            double estimatedHourlyProfitEur
    ) {}

    public HybridDesalHydrogenCluster optimizeMpcDispatch(double renewablePowerMw, double spotPriceEurMwh) {
        // Asignación multivariable MPC
        double totalRenewable = Math.min(renewablePowerMw, capacities.electrolyzerMaxMw() + (capacities.desalMaxM3Day() * 0.0035 / 24.0)); // 3.5 kWh/m3 desal

        double electrolyzerMw = 0.0;
        double desalMw = 0.0;

        if (spotPriceEurMwh < 35.0) {
            // Precio bajo o vertido -> Priorizar electrólisis H2 intensiva
            electrolyzerMw = Math.min(totalRenewable * 0.75, capacities.electrolyzerMaxMw());
            desalMw = totalRenewable - electrolyzerMw;
        } else {
            // Precio alto -> Priorizar desalación base y modular H2
            desalMw = Math.min(totalRenewable * 0.40, capacities.desalMaxM3Day() * 0.0035 / 24.0);
            electrolyzerMw = totalRenewable - desalMw;
        }

        double h2KgHour = (electrolyzerMw * 1000.0) / 50.0; // ~50 kWh/kg H2
        double waterM3Hour = (desalMw * 1000.0) / 3.5; // ~3.5 kWh/m3

        double revenueH2 = h2KgHour * 6.50; // 6.50 EUR/kg H2 verde
        double revenueWater = waterM3Hour * 0.95; // 0.95 EUR/m3
        double energyCost = totalRenewable * spotPriceEurMwh;
        double profit = (revenueH2 + revenueWater) - energyCost;

        CurrentOperatingState nextState = new CurrentOperatingState(
                renewablePowerMw, spotPriceEurMwh, h2KgHour, waterM3Hour
        );
        MpcDispatchSetpoint nextSetpoint = new MpcDispatchSetpoint(
                electrolyzerMw, desalMw, h2KgHour, waterM3Hour, profit
        );

        return new HybridDesalHydrogenCluster(
                this.id,
                this.tenantId,
                this.capacities,
                nextState,
                nextSetpoint,
                Instant.now()
        );
    }
}
