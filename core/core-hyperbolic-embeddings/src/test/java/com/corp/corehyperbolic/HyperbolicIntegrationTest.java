package com.corp.corehyperbolic;

import com.corp.corehyperbolic.application.HyperbolicHierarchyProjectionUseCase;
import com.corp.corehyperbolic.domain.HyperbolicPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HyperbolicIntegrationTest {

    @Test
    @DisplayName("Debe calcular distancia entre nodo padre e hijo en espacio de Poincaré")
    void testHyperbolicHierarchyIntegration() {
        HyperbolicHierarchyProjectionUseCase useCase = new HyperbolicHierarchyProjectionUseCase();
        HyperbolicPoint root = new HyperbolicPoint("ROOT", new double[]{0.0, 0.0}, 1.0);
        HyperbolicPoint child = useCase.projectChild("CHILD_01", root, 0.5);

        double dist = useCase.calculateHierarchyDistance(root, child);

        assertNotNull(child);
        assertTrue(dist > 0.0);
        assertEquals("CHILD_01", child.nodeIdentifier());
    }
}
