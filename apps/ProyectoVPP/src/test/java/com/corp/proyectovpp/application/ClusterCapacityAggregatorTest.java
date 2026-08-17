package com.corp.proyectovpp.application;

import com.corp.proyectovpp.domain.model.BatteryEnergyStorageUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link ClusterCapacityAggregator}.
 */
class ClusterCapacityAggregatorTest {

    private final ClusterCapacityAggregator aggregator = new ClusterCapacityAggregator();

    @Test
    @DisplayName("Debe agregar correctamente la capacidad de una flota de baterías BESS")
    void shouldAggregateBessUnitsCorrectly() {
        BatteryEnergyStorageUnit bess1 = new BatteryEnergyStorageUnit("BESS_01", "LFP", 80.0, 100.0, Instant.now());
        BatteryEnergyStorageUnit bess2 = new BatteryEnergyStorageUnit("BESS_02", "NMC", 50.0, 200.0, Instant.now());

        ClusterCapacityAggregator.AggregatedClusterState state = aggregator.aggregateCluster(List.of(bess1, bess2));

        assertEquals(2, state.totalUnits());
        assertEquals(300.0, state.totalInstalledCapacityKwh(), 0.001);

        // bess1 descargable: 100 * (80-10)/100 = 70 kWh
        // bess2 descargable: 200 * (50-10)/100 = 80 kWh
        // Total descargable: 150 kWh
        assertEquals(150.0, state.availableDischargeCapacityKwh(), 0.001);

        // bess1 recargable: 100 * (95-80)/100 = 15 kWh
        // bess2 recargable: 200 * (95-50)/100 = 90 kWh
        // Total recargable: 105 kWh
        assertEquals(105.0, state.availableChargeCapacityKwh(), 0.001);

        assertTrue(state.spinningReserveKw() > 0.0);
    }

    @Test
    @DisplayName("Debe manejar listas vacías de forma segura")
    void shouldHandleEmptyListSafely() {
        ClusterCapacityAggregator.AggregatedClusterState state = aggregator.aggregateCluster(List.of());

        assertEquals(0, state.totalUnits());
        assertEquals(0.0, state.totalInstalledCapacityKwh(), 0.001);
        assertEquals(0.0, state.availableDischargeCapacityKwh(), 0.001);
    }
}
