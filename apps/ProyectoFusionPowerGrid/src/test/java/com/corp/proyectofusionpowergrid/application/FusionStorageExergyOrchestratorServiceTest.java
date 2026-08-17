package com.corp.proyectofusionpowergrid.application;

import com.corp.proyectofusionpowergrid.application.service.FusionStorageExergyOrchestratorService;
import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FusionStorageExergyOrchestratorServiceTest {

    @Test
    @DisplayName("Debe coordinar ciclo sinérgico de reactor Tokamak, cálculo de exergía Gouy-Stodola y carga BESS")
    void testFusionStorageExergySynergy() {
        var orchestrator = new FusionStorageExergyOrchestratorService();

        TokamakPlasmaState plasma = TokamakPlasmaState.create("ITER-DEMO-2026", 5.3);
        BatteryStorageUnit bess = BatteryStorageUnit.create("BESS-GRID-VALENCIA-01", 5000.0, 1000.0);

        var result = orchestrator.coordinateFusionAndStorageDispatch(
                plasma,
                bess,
                50.0,   // 50 kg/s helio
                850.0,  // 850 K refrigerante primario
                293.15  // 293.15 K ambiente
        );

        assertNotNull(result);
        assertEquals("ITER-DEMO-2026", result.reactorId());
        assertEquals("BESS-GRID-VALENCIA-01", result.batteryId());
        assertTrue(result.generatedExergyMw() > 0.0);
        assertTrue(result.transferredToStorageKwh() > 0.0);
        assertTrue(result.dispatchOptimal());
    }
}
