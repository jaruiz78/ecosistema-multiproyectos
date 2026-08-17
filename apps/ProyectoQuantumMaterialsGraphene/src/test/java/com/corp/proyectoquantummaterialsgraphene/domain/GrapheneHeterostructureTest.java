package com.corp.proyectoquantummaterialsgraphene.domain;

import com.corp.proyectoquantummaterialsgraphene.domain.model.GrapheneHeterostructure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrapheneHeterostructureTest {

    @Test
    @DisplayName("Debe detectar fase superconductora no convencional para ángulo mágico de 1.1 grados")
    void testMagicAngleSuperconductivity() {
        GrapheneHeterostructure g = GrapheneHeterostructure.create("MAGIC-SAMPLE-01", 1.10);

        assertEquals(1.7, g.criticalTemperatureKelvin(), 1e-3);
        assertEquals(GrapheneHeterostructure.SuperconductingPhase.UNCONVENTIONAL_SUPERCONDUCTING, g.phase());
    }
}
