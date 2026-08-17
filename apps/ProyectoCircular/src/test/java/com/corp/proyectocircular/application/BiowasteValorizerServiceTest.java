package com.corp.proyectocircular.application;

import com.corp.proyectocircular.domain.model.BiowasteBatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link BiowasteValorizerService}.
 */
class BiowasteValorizerServiceTest {

    private final BiowasteValorizerService service = new BiowasteValorizerService();

    @Test
    @DisplayName("Debe calcular balances de biometano, energía neta y cinética de Arrhenius correctamente")
    void shouldCalculateValorizationCorrectly() {
        // Lote de 10.000 kg (10 toneladas) con 75% de humedad, BMP = 120 Nm3/ton
        BiowasteBatch batch = new BiowasteBatch(
                "BATCH_AGRO_001",
                "TENANT_CIRCULAR_ES",
                10000.0,
                75.0,
                120.0,
                25.0,
                Instant.now()
        );

        // Operación mesofílica a 37°C
        BiowasteValorizerService.ValorizationYield yield = service.calculateValorization(batch, 37.0);

        assertNotNull(yield);
        assertEquals("BATCH_AGRO_001", yield.batchId());
        // Materia seca: 10000 * 0.25 = 2500 kg
        assertEquals(2500.0, yield.dryMatterMassKg(), 0.001);

        // Biogás: 10 ton * 120 Nm3/ton = 1200 Nm3
        assertEquals(1200.0, yield.theoreticalBiogasVolumeNm3(), 0.001);

        // Biometano: 1200 * 0.65 = 780 Nm3
        assertEquals(780.0, yield.biomethaneVolumeNm3(), 0.001);

        // Energía: 780 * 9.97 = 7776.6 kWh
        assertEquals(7776.6, yield.totalEnergyYieldKwh(), 0.01);

        // Cinética positiva y coherente
        assertTrue(yield.kineticRatePerDay() > 0.0);

        // Compensación de CO2 > 0
        assertTrue(yield.carbonOffsetKgCo2() > 0.0);
    }

    @Test
    @DisplayName("Debe incrementar la tasa cinética de Arrhenius a mayor temperatura (sensibilidad térmica)")
    void shouldIncreaseKineticRateWithHigherTemperature() {
        BiowasteBatch batch = new BiowasteBatch(
                "BATCH_002",
                "TENANT_CIRCULAR_ES",
                5000.0,
                80.0,
                100.0,
                20.0,
                Instant.now()
        );

        BiowasteValorizerService.ValorizationYield mesophilic = service.calculateValorization(batch, 35.0);
        BiowasteValorizerService.ValorizationYield thermophilic = service.calculateValorization(batch, 55.0);

        assertTrue(thermophilic.kineticRatePerDay() > mesophilic.kineticRatePerDay(),
                "La tasa cinética termofílica (55°C) debe ser mayor que la mesofílica (35°C)");
    }
}
