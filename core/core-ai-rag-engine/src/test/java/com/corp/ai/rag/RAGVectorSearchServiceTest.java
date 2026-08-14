package com.corp.ai.rag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("RAGVectorSearchService - Tests de Similitud Coseno SIMD y Búsqueda Vectorial")
class RAGVectorSearchServiceTest {

    @Test
    @DisplayName("Debe calcular similitud coseno exacta para vectores idénticos (1.0) y ortogonales (0.0)")
    void testCosineSimilarityIdenticalAndOrthogonal() {
        float[] a = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        float[] b = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        float[] c = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

        double simIdentical = RAGVectorSearchService.computeCosineSimilarity(a, b);
        assertEquals(1.0, simIdentical, 0.0001);

        double simOrthogonal = RAGVectorSearchService.computeCosineSimilarity(a, c);
        assertEquals(0.0, simOrthogonal, 0.0001);
    }

    @Test
    @DisplayName("Debe manejar vectores con longitudes no múltiplos de 4 (restos de frontera SIMD)")
    void testCosineSimilarityBoundaryLengths() {
        float[] v1 = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f}; // len 5
        float[] v2 = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f};

        double sim = RAGVectorSearchService.computeCosineSimilarity(v1, v2);
        assertEquals(1.0, sim, 0.0001);
    }

    @Test
    @DisplayName("Debe lanzar excepción si las dimensiones de los vectores no coinciden o son nulos")
    void testDimensionMismatchAndNulls() {
        assertThrows(IllegalArgumentException.class, () -> RAGVectorSearchService.computeCosineSimilarity(null, new float[]{1.0f}));
        assertThrows(IllegalArgumentException.class, () -> RAGVectorSearchService.computeCosineSimilarity(new float[]{1.0f}, new float[]{1.0f, 2.0f}));
    }

    @Test
    @DisplayName("Debe recuperar matches relevantes con searchRelevantKnowledge")
    void testSearchRelevantKnowledge() {
        float[] query = new float[]{0.5f, -0.5f, 0.2f, 0.8f};
        List<RAGVectorSearchService.VectorMatch> matches = RAGVectorSearchService.searchRelevantKnowledge(query, "Snippet de contexto");

        assertNotNull(matches);
        assertFalse(matches.isEmpty());
        assertEquals(1.0, matches.get(0).similarityScore(), 0.0001);
        assertEquals("Snippet de contexto", matches.get(0).textSnippet());
    }
}
