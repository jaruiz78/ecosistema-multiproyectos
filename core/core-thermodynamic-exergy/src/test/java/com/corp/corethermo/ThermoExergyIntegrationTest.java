package com.corp.corethermo;

import com.corp.corethermo.application.DistrictHeatingExergyOptimizationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThermoExergyIntegrationTest {

    @Test
    @DisplayName("Debe auditar lazo de calefacción urbana y calcular eficiencia exergética")
    void testAuditDistrictHeatingIntegration() {
        DistrictHeatingExergyOptimizationUseCase useCase = new DistrictHeatingExergyOptimizationUseCase();

        var report = useCase.auditDistrictHeatingLoop(
                "HEATING-DISTRICT-VALENCIA-01",
                25.0,  // 25 kg/s
                363.15, // 90 C suministro
                333.15, // 60 C retorno
                288.15  // 15 C ambiente
        );

        assertNotNull(report);
        assertEquals("HEATING-DISTRICT-VALENCIA-01", report.plantId());
        assertTrue(report.physicalExergyInKw() > 0.0);
        assertTrue(report.secondLawEfficiencyPct() > 0.0);
    }
}
