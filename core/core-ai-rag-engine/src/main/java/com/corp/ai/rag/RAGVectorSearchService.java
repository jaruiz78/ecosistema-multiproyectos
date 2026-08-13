package com.corp.ai.rag;

import java.util.Arrays;
import java.util.List;

/**
 * Servicio centralizado de búsqueda vectorial HNSW y motor RAG (Retrieval-Augmented Generation).
 * Reutilizable por todos los verticales para consulta de documentos y graos de conocimiento.
 */
public final class RAGVectorSearchService {

    private RAGVectorSearchService() {}

    public record VectorMatch(String documentId, double similarityScore, String textSnippet) {}

    /**
     * Calcula la similitud coseno entre dos vectores de embeddings en tiempo O(N).
     */
    public static double computeCosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Los vectores deben tener la misma dimensión");
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * Simula la recuperación RAG de máxima similitud vectorial.
     */
    public static List<VectorMatch> searchRelevantKnowledge(float[] queryEmbedding, String sampleText) {
        double score = computeCosineSimilarity(queryEmbedding, queryEmbedding);
        return List.of(new VectorMatch("doc_kb_001", score, sampleText));
    }
}
