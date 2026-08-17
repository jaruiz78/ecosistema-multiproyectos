package com.corp.core.math.se3;

import com.corp.corese3.domain.Protein3DAtomicEmbedding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Se3EquivariantGraphFilterTest {

    @Test
    @DisplayName("Debe calcular distancia invariante por pares y energía conformacional en SE(3)")
    void testSe3InvarianceAndEnergy() {
        var a1 = Protein3DAtomicEmbedding.create("C_ALPHA_1", 0.0, 0.0, 0.0, 12.011);
        var a2 = Protein3DAtomicEmbedding.create("N_AMINE_2", 1.5, 0.0, 0.0, 14.007);

        double dist = Se3EquivariantGraphFilter.computePairwiseDistance(a1, a2);
        assertEquals(1.5, dist, 1e-4);

        double energy = Se3EquivariantGraphFilter.computeSe3MolecularEnergyScore(List.of(a1, a2));
        assertNotNull(energy);
    }
}
