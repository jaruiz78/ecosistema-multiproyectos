package com.corp.coreneuromorphic;

import com.corp.coreneuromorphic.application.NeuromorphicSpikeProcessingUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeuromorphicIntegrationTest {

    @Test
    @DisplayName("Debe procesar secuencia de señales y calcular tasa de disparo energética")
    void testProcessSignalStreamIntegration() {
        NeuromorphicSpikeProcessingUseCase useCase = new NeuromorphicSpikeProcessingUseCase();
        double[] signal = new double[]{10.0, 20.0, 30.0, 50.0, 10.0, 5.0, 50.0, 2.0, 1.0, 50.0};

        var result = useCase.processSignalStream("NET-SNN-01", signal, 100.0, 10.0);

        assertNotNull(result);
        assertEquals("NET-SNN-01", result.networkId());
        assertTrue(result.activeSpikeRateHz() >= 0.0);
    }
}
