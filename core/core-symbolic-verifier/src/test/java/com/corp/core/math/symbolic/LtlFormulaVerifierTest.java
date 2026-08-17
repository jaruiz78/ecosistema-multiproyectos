package com.corp.core.math.symbolic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LtlFormulaVerifierTest {

    @Test
    @DisplayName("Debe verificar Always (P) correctamente en traza válida e inválida")
    void testVerifyAlways() {
        List<Integer> validTrace = List.of(2, 4, 6, 8, 10);
        List<Integer> invalidTrace = List.of(2, 4, 5, 8, 10);

        assertTrue(LtlFormulaVerifier.verifyAlways(validTrace, x -> x % 2 == 0));
        assertFalse(LtlFormulaVerifier.verifyAlways(invalidTrace, x -> x % 2 == 0));
    }

    @Test
    @DisplayName("Debe verificar Response (Always(P -> Eventually Q))")
    void testVerifyResponse() {
        // P: es 1, Q: es 2
        List<Integer> validTrace = List.of(0, 1, 0, 2, 0);
        List<Integer> invalidTrace = List.of(0, 1, 0, 0, 0);

        assertTrue(LtlFormulaVerifier.verifyResponse(validTrace, x -> x == 1, x -> x == 2));
        assertFalse(LtlFormulaVerifier.verifyResponse(invalidTrace, x -> x == 1, x -> x == 2));
    }

    @Test
    @DisplayName("Debe detectar violación en RuntimeContractMonitor")
    void testRuntimeContractMonitor() {
        RuntimeContractMonitor<Double> monitor = RuntimeContractMonitor.create("TEMP-SENSOR-01");
        monitor = monitor.appendState(25.0, t -> t < 80.0, "Temperatura < 80C");
        assertFalse(monitor.violationDetected());

        monitor = monitor.appendState(95.0, t -> t < 80.0, "Temperatura < 80C");
        assertTrue(monitor.violationDetected());
        assertNotNull(monitor.lastViolationReason());
    }
}
