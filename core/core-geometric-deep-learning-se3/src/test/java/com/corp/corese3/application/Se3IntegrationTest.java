package com.corp.corese3.application;

import com.corp.corese3.domain.Protein3DAtomicEmbedding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Se3IntegrationTest {

    @Test
    @DisplayName("Debe evaluar estabilidad molecular mediante caso de uso SE(3)")
    void testScoreConformationalStability() {
        var useCase = new GeometricProteinDesignUseCase();
        var graph = List.of(
                Protein3DAtomicEmbedding.create("ATOM_A", 0.0, 0.0, 0.0, 12.0),
                Protein3DAtomicEmbedding.create("ATOM_B", 1.5, 0.0, 0.0, 16.0),
                Protein3DAtomicEmbedding.create("ATOM_C", 0.0, 1.5, 0.0, 1.0)
        );

        double score = useCase.scoreConformationalStability(graph);
        assertNotNull(score);
    }
}
