package com.corp.coreqaoa;

import com.corp.coreqaoa.application.QaoaCombinatorialOptimizationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QaoaIntegrationTest {

    @Test
    @DisplayName("Debe ejecutar QAOA y resolver partición de grafo Max-Cut")
    void testSolveMaxCutGraphIntegration() {
        QaoaCombinatorialOptimizationUseCase useCase = new QaoaCombinatorialOptimizationUseCase();
        double[][] adj = new double[][]{
                {0.0, 1.0, 1.0},
                {1.0, 0.0, 1.0},
                {1.0, 1.0, 0.0}
        };

        var result = useCase.solveMaxCutGraph("GRAPH-TRIANGLE-01", adj);

        assertNotNull(result);
        assertEquals(3, result.numNodes());
        assertEquals(3, result.optimalSpinPartition().length);
        assertTrue(result.quantumSpeedupAchieved());
    }
}
