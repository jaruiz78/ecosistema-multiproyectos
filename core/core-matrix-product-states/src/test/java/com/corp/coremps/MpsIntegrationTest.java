package com.corp.coremps;

import com.corp.coremps.application.QuantumTensorCompressionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MpsIntegrationTest {

    @Test
    @DisplayName("Debe comprimir estado cuántico de 20 qubits logrando compresión masiva >90%")
    void testQuantumTensorCompressionIntegration() {
        QuantumTensorCompressionUseCase useCase = new QuantumTensorCompressionUseCase();
        var report = useCase.compressQuantumGridState("QPU-SIM-STATE-20Q", 20, 4);

        assertNotNull(report);
        assertEquals(20, report.totalQubits());
        assertTrue(report.compressionRatioPct() > 90.0);
        assertTrue(report.fidelityPreserved());
    }
}
