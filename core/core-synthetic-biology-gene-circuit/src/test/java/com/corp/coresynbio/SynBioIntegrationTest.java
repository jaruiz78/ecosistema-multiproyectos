package com.corp.coresynbio;

import com.corp.coresynbio.application.SyntheticGeneCircuitSimulationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SynBioIntegrationTest {

    @Test
    @DisplayName("Debe simular circuito genético AND y activar expresión con inductores altos")
    void testSimulateBiosensorGateIntegration() {
        SyntheticGeneCircuitSimulationUseCase useCase = new SyntheticGeneCircuitSimulationUseCase();
        var profile = useCase.simulateBiosensorGate("CIRCUIT-PFAS-SENSOR-01", "Pseudomonas_putida_KT2440", 15.0, 15.0);

        assertNotNull(profile);
        assertEquals("CIRCUIT-PFAS-SENSOR-01", profile.circuitId());
        assertTrue(profile.proteinOutputRpu() > 5.0);
        assertTrue(profile.logicStateHigh());
    }
}
