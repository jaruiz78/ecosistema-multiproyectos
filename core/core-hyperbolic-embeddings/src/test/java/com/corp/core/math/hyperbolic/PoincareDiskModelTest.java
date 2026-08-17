package com.corp.core.math.hyperbolic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoincareDiskModelTest {

    @Test
    @DisplayName("La distancia hiperbólica de un punto a sí mismo debe ser 0")
    void testSelfDistanceZero() {
        double[] u = new double[]{0.2, 0.3};
        double dist = PoincareDiskModel.distance(u, u);
        assertEquals(0.0, dist, 1e-6);
    }

    @Test
    @DisplayName("La distancia hacia el borde del disco unitario tiende a infinito")
    void testBoundaryDivergence() {
        double[] center = new double[]{0.0, 0.0};
        double[] nearBoundary = new double[]{0.999, 0.0};

        double dist = PoincareDiskModel.distance(center, nearBoundary);
        assertTrue(dist > 7.0); // ln(2 / (1 - 0.999^2)) diverge
    }

    @Test
    @DisplayName("Debe lanzar excepción si el punto está en o fuera del disco unitario")
    void testOutOfBoundsException() {
        double[] invalid = new double[]{1.0, 0.0};
        double[] valid = new double[]{0.1, 0.1};

        assertThrows(IllegalArgumentException.class, () -> PoincareDiskModel.distance(invalid, valid));
    }
}
