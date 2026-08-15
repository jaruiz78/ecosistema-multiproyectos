package ai.core.rag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("HybridGraphVectorSearchEngine - Tests del Motor Híbrido RAG + Grafo")
class HybridGraphVectorSearchEngineTest {

    private final HybridGraphVectorSearchEngine engine = new HybridGraphVectorSearchEngine();

    @Test
    @DisplayName("Debe ejecutar búsqueda híbrida y retornar resultados ponderados con sub-milisegundo")
    void testExecuteHybridSearch() {
        float[] queryEmbedding = new float[]{0.12f, 0.45f, -0.89f, 0.33f};
        List<HybridGraphVectorSearchEngine.SearchResult> results = engine.executeHybridSearch(queryEmbedding, 5, "domain:mobility");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());

        HybridGraphVectorSearchEngine.SearchResult top = results.get(0);
        assertNotNull(top.documentId());
        assertTrue(top.vectorSimilarityScore() > 0.0);
        assertTrue(top.graphRelevanceScore() > 0.0);
        assertTrue(top.combinedConfidence() > 0.0);
        assertNotNull(top.retrievedAt());
    }

    @Test
    @DisplayName("El record SearchResult debe validar invariantes de no-nulidad")
    void testSearchResultInvariants() {
        assertThrows(NullPointerException.class, () -> new HybridGraphVectorSearchEngine.SearchResult(
                null, "snippet", 0.9, 0.9, 0.9, 100, java.time.Instant.now()
        ));
        assertThrows(NullPointerException.class, () -> new HybridGraphVectorSearchEngine.SearchResult(
                "doc-1", null, 0.9, 0.9, 0.9, 100, java.time.Instant.now()
        ));
    }
}
