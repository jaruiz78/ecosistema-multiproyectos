package com.corp.coretda;

import com.corp.coretda.application.TdaAnomalyDetectionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TdaIntegrationTest {

    @Test
    @DisplayName("Debe analizar firma topológica de sensores y detectar cavidades")
    void testAnalyzeStructuralMeshIntegration() {
        TdaAnomalyDetectionUseCase useCase = new TdaAnomalyDetectionUseCase();
        double[][] sensorGrid = new double[][]{
                {0.0, 0.0},
                {10.0, 0.0},
                {10.0, 10.0},
                {0.0, 10.0}
        };

        var signature = useCase.analyzeStructuralMesh("DAM-SCADA-SECTOR-4", sensorGrid, 15.0);

        assertNotNull(signature);
        assertEquals("DAM-SCADA-SECTOR-4", signature.sensorMeshId());
        assertTrue(signature.betti0ConnectedComponents() >= 1);
    }
}
