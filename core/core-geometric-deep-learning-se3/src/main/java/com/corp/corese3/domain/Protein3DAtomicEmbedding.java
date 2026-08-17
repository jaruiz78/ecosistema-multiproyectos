package com.corp.corese3.domain;

import java.io.Serializable;

/**
 * Representa el embedding atómico tridimensional de una proteína con coordenadas espaciales y tensores invariantes.
 */
public record Protein3DAtomicEmbedding(
        String atomIdentifier,
        double coordX,
        double coordY,
        double coordZ,
        double atomicMassDaltons,
        double invariantScalarFeature
) implements Serializable {

    public static Protein3DAtomicEmbedding create(String id, double x, double y, double z, double mass) {
        double scalar = Math.sqrt(x * x + y * y + z * z) * mass;
        return new Protein3DAtomicEmbedding(id, x, y, z, mass, scalar);
    }
}
