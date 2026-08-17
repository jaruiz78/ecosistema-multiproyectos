package com.corp.coredigitallaw;

import com.corp.coredigitallaw.application.DigitalLawVerificationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigitalLawIntegrationTest {

    @Test
    @DisplayName("Debe auditar sistema de IA bajo EU AI Act y generar informe conforme")
    void testAuditAiSystemIntegration() {
        DigitalLawVerificationUseCase useCase = new DigitalLawVerificationUseCase();

        var audit = useCase.auditAiSystem(
                "AUDIT-AI-2026-001",
                "SCADA-TWIN-AUTONOMOUS",
                true,
                true,
                true,
                true
        );

        assertNotNull(audit);
        assertEquals("AUDIT-AI-2026-001", audit.auditId());
        assertTrue(audit.compliant());
    }
}
