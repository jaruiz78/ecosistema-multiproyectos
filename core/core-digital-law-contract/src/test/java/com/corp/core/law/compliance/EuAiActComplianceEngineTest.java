package com.corp.core.law.compliance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EuAiActComplianceEngineTest {

    @Test
    @DisplayName("Debe clasificar como alto riesgo y exigir artículos 9, 11 y 14")
    void testHighRiskRequirements() {
        var incompleteProfile = new EuAiActComplianceEngine.SystemProfile(
                "GRID-OPTIMIZER-01",
                false,
                true, // Infraestructura crítica
                false,
                false, // Falta supervisión humana
                true,
                false  // Falta gestión continua de riesgos
        );

        var report = EuAiActComplianceEngine.evaluate(incompleteProfile);

        assertEquals(EuAiActComplianceEngine.RiskCategory.HIGH_RISK, report.riskCategory());
        assertFalse(report.compliant());
        assertEquals(2, report.missingRequirements().size());
    }

    @Test
    @DisplayName("Debe aprobar sistema de alto riesgo cuando cumple todos los requisitos regulatorios")
    void testHighRiskCompliant() {
        var completeProfile = new EuAiActComplianceEngine.SystemProfile(
                "GRID-OPTIMIZER-PRO",
                false,
                true,
                false,
                true,
                true,
                true
        );

        var report = EuAiActComplianceEngine.evaluate(completeProfile);

        assertTrue(report.compliant());
        assertTrue(report.missingRequirements().isEmpty());
    }

    @Test
    @DisplayName("Debe validar reglas del pasaporte digital de producto (DPP)")
    void testDppValidation() {
        var validDpp = new DigitalProductPassportRuleEvaluator.DppPayload(
                "BATCH-RECYCLED-001",
                12.5,
                85.0,
                true,
                true
        );

        var result = DigitalProductPassportRuleEvaluator.evaluateDpp(validDpp);
        assertTrue(result.approved());
        assertTrue(result.violations().isEmpty());
    }
}
