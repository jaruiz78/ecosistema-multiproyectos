package com.corp.core.robotics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialEuclideanTransformationTest {

    @Test
    @DisplayName("Debe calcular distancia geodésica Riemanniana en el grupo de Lie SE(3)")
    void shouldComputeGeodesicDistanceOnSE3() {
        SpecialEuclideanTransformation transform = SpecialEuclideanTransformation.fromPose(
                "POSE-ROBOT-01",
                3.0, 4.0, 0.0, // translation dist = 5.0
                1.0, 0.0, 0.0, 0.0 // qw = 1.0 (rot = 0 rad)
        );

        assertNotNull(transform);
        assertEquals(5.0, transform.geodesicDistanceToOrigin(), 1e-4);
        assertEquals(3.0, transform.translationVector()[0]);
    }
}
