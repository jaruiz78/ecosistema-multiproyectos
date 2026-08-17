package com.corp.proyectovpp.application;

import com.corp.proyectovpp.domain.model.BatteryEnergyStorageUnit;

import java.util.List;
import java.util.Objects;

/**
 * Agregador de Capacidad y Despacho Óptimo para Centrales Eléctricas Virtuales (VPP / DERs).
 * Modela la agregación de recursos distribuidos de almacenamiento (BESS), reserva rodante
 * y límites de rampa en tiempo real en O(N).
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public final class ClusterCapacityAggregator {

    private static final double MIN_USABLE_SOC_PERCENT = 10.0; // Reserva mínima de seguridad (10%)
    private static final double MAX_USABLE_SOC_PERCENT = 95.0; // Límite de sobrecarga (95%)
    private static final double DEFAULT_C_RATE = 1.0; // Capacidad de descarga 1C estándar (1 hora)

    /**
     * Resultado consolidado de la agregación de un cluster VPP.
     */
    public record AggregatedClusterState(
            int totalUnits,
            double totalInstalledCapacityKwh,
            double availableDischargeCapacityKwh,
            double availableChargeCapacityKwh,
            double maxDischargePowerKw,
            double maxChargePowerKw,
            double averageStateOfChargePercent,
            double spinningReserveKw
    ) {}

    /**
     * Agrega la capacidad disponible y potencia de rampa de una flota BESS en O(N).
     *
     * @param units Lista de unidades BESS distribuidas en la red.
     * @return {@link AggregatedClusterState} con el estado agregado.
     */
    public AggregatedClusterState aggregateCluster(List<BatteryEnergyStorageUnit> units) {
        Objects.requireNonNull(units, "La lista de unidades no puede ser nula");
        if (units.isEmpty()) {
            return new AggregatedClusterState(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        }

        double totalInstalledKwh = 0.0;
        double availableDischargeKwh = 0.0;
        double availableChargeKwh = 0.0;
        double totalWeightedSoc = 0.0;

        for (BatteryEnergyStorageUnit unit : units) {
            double cap = unit.capacityKwh();
            double soc = unit.stateOfChargePercent();

            totalInstalledKwh += cap;
            totalWeightedSoc += soc * cap;

            // Energía neta descargable respetando SoC mínimo
            if (soc > MIN_USABLE_SOC_PERCENT) {
                double usableSocDelta = soc - MIN_USABLE_SOC_PERCENT;
                availableDischargeKwh += cap * (usableSocDelta / 100.0);
            }

            // Energía neta recargable respetando SoC máximo
            if (soc < MAX_USABLE_SOC_PERCENT) {
                double headroomSocDelta = MAX_USABLE_SOC_PERCENT - soc;
                availableChargeKwh += cap * (headroomSocDelta / 100.0);
            }
        }

        double avgSoc = (totalInstalledKwh > 0.0) ? (totalWeightedSoc / totalInstalledKwh) : 0.0;
        double maxDischargePowerKw = availableDischargeKwh * DEFAULT_C_RATE;
        double maxChargePowerKw = availableChargeKwh * DEFAULT_C_RATE;
        double spinningReserveKw = maxDischargePowerKw * 0.20; // 20% reservado para regulación de frecuencia

        return new AggregatedClusterState(
                units.size(),
                totalInstalledKwh,
                availableDischargeKwh,
                availableChargeKwh,
                maxDischargePowerKw,
                maxChargePowerKw,
                avgSoc,
                spinningReserveKw
        );
    }
}
