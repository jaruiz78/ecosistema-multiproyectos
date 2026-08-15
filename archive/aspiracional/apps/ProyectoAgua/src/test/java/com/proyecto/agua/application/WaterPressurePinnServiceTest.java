package com.proyecto.agua.application;

import com.proyecto.agua.domain.WaterNetworkNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias Zero-Mockito para la evaluación de transitorios de presión hidráulica en ProyectoAgua.
 */
class WaterPressurePinnServiceTest {

    @Test
    void testEvaluatePressureTransientNormalFlow() {
        WaterPressurePinnService service = new WaterPressurePinnService();
        WaterNetworkNode initialNode = new WaterNetworkNode("node_hydra_001", "8828308281fffff", 3.5, 45.0, false);

        WaterNetworkNode evaluated = service.evaluatePressureTransient(initialNode, 1200.0);

        assertNotNull(evaluated);
        assertEquals("node_hydra_001", evaluated.nodeId());
        assertTrue(evaluated.pressureBar() > 3.5);
        assertFalse(evaluated.anomalyDetected());
    }

    @Test
    void testEvaluatePressureTransientAnomalyDetection() {
        WaterPressurePinnService service = new WaterPressurePinnService();
        WaterNetworkNode highPressureNode = new WaterNetworkNode("node_hydra_002", "8828308281fffff", 5.8, 120.0, false);

        WaterNetworkNode evaluated = service.evaluatePressureTransient(highPressureNode, 1200.0);

        assertTrue(evaluated.pressureBar() > 6.0);
        assertTrue(evaluated.anomalyDetected());
    }
}
