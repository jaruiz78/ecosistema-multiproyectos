package com.corp.corese3.application;

import com.corp.core.math.se3.Se3EquivariantGraphFilter;
import com.corp.corese3.domain.Protein3DAtomicEmbedding;

import java.io.Serializable;
import java.util.List;

/**
 * Caso de uso para diseñar y puntuar la estabilidad conformacional de proteínas y enzimas en SE(3).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GeometricProteinDesignUseCase implements Serializable {

    public double scoreConformationalStability(List<Protein3DAtomicEmbedding> atomicGraph) {
        return Se3EquivariantGraphFilter.computeSe3MolecularEnergyScore(atomicGraph);
    }
}
