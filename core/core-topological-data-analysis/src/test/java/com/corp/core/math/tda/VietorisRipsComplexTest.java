package com.corp.core.math.tda;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VietorisRipsComplexTest {

    @Test
    @DisplayName("Debe calcular homología persistente para nube de puntos en anillo")
    void testComputePersistenceRing() {
        // Puntos formando un triángulo / lazo
        double[][] points = new double[][]{
                {0.0, 0.0},
                {1.0, 0.0},
                {0.5, 0.866}
        };

        List<PersistenceDiagram> diagrams = VietorisRipsComplex.computePersistence(points, 2.0);

        assertNotNull(diagrams);
        assertFalse(diagrams.isEmpty());
        assertEquals(0, diagrams.get(0).dimension());
    }

    @Test
    @DisplayName("Debe manejar nubes vacías sin fallar")
    void testEmptyPointCloud() {
        List<PersistenceDiagram> diagrams = VietorisRipsComplex.computePersistence(new double[][]{}, 1.0);
        assertTrue(diagrams.isEmpty());
    }
}
