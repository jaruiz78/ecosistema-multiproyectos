package com.corp.corese3.application;

import com.corp.core.math.se3.Se3EquivariantGraphFilter;
import com.corp.corese3.domain.Protein3DAtomicEmbedding;

import java.io.Serializable;
import java.util.List;

/**
 * Caso de uso para diseñar y puntuar la estabilidad conformacional de proteínas y enzimas en SE(3).
 */
public class GeometricProteinDesignUseCase implements Serializable {

    public double scoreConformationalStability(List<Protein3DAtomicEmbedding> atomicGraph) {
        return Se3EquivariantGraphFilter.computeSe3MolecularEnergyScore(atomicGraph);
    }
}
