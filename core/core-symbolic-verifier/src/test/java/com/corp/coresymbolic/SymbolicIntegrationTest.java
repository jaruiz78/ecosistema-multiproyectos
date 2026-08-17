package com.corp.coresymbolic;

import com.corp.coresymbolic.application.SymbolicModelCheckingUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SymbolicIntegrationTest {

    @Test
    @DisplayName("Debe verificar traza de seguridad formal en use case")
    void testVerifySafetyTraceIntegration() {
        SymbolicModelCheckingUseCase useCase = new SymbolicModelCheckingUseCase();
        List<Double> pressureTrace = List.of(1.1, 1.2, 1.3, 1.25, 1.15);

        var result = useCase.verifySafetyTrace("TRACE-PRES-001", pressureTrace, p -> p < 2.0);

        assertNotNull(result);
        assertTrue(result.valid());
        assertEquals(5, result.totalStatesEvaluated());
    }
}
