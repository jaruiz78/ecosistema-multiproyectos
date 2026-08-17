package com.corp.proyectohidrogeno.application;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas TDD Zero-Mockito para {@link AgroVoltaicHydrogenDispatcherService}.
 */
class AgroVoltaicHydrogenDispatcherServiceTest {

    @Test
    @DisplayName("Debe calcular el balance de hidrógeno y agua estequiométrica correctamente")
    void shouldComputeHydrogenAndWaterBalancesCorrectly() {
        AgroVoltaicHydrogenDispatcherService service = new AgroVoltaicHydrogenDispatcherService();

        HydrogenProductionBatch batch = new HydrogenProductionBatch(
                UUID.randomUUID().toString(),
                "TENANT_AGRO_01",
                "BATCH_SOLAR_H2_01",
                100.0,
                "PLANNED",
                Instant.now()
        );

        // Excedente de 1000 kWh solar con 80% de eficiencia de stack PEM
        // Energía efectiva: 800 kWh -> 800 / 50 = 16.0 kg H2 -> 16 * 9 = 144.0 L H2O
        var plan = service.computeDispatch(batch, 1000.0, 0.80);

        assertEquals(16.0, plan.hydrogenProducedKg(), 1e-4);
        assertEquals(144.0, plan.waterConsumedLiters(), 1e-4);
        assertEquals(1000.0, plan.surplusPowerUtilizedKwh(), 1e-4);
    }

    @Test
    @DisplayName("Debe rechazar eficiencias fuera del intervalo (0, 1]")
    void shouldRejectInvalidEfficiency() {
        AgroVoltaicHydrogenDispatcherService service = new AgroVoltaicHydrogenDispatcherService();

        HydrogenProductionBatch batch = new HydrogenProductionBatch(
                UUID.randomUUID().toString(),
                "TENANT_AGRO_01",
                "BATCH_ERR",
                1.0,
                "PLANNED",
                Instant.now()
        );

        assertThrows(IllegalArgumentException.class, () ->
                service.computeDispatch(batch, 500.0, 1.2)
        );
        assertThrows(IllegalArgumentException.class, () ->
                service.computeDispatch(batch, 500.0, 0.0)
        );
    }
}
