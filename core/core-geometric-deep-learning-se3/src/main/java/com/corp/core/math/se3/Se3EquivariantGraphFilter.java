package com.corp.core.math.se3;

import com.corp.corese3.domain.Protein3DAtomicEmbedding;

import java.io.Serializable;
import java.util.List;

/**
 * Filtro de convolución sobre grafos moleculares equivariante bajo rototranslaciones en el grupo euclidiano SE(3).
 */
public class Se3EquivariantGraphFilter implements Serializable {

    /**
     * Calcula la distancia euclidiana invariante entre dos átomos en el espacio 3D.
     */
    public static double computePairwiseDistance(Protein3DAtomicEmbedding a, Protein3DAtomicEmbedding b) {
        double dx = a.coordX() - b.coordX();
        double dy = a.coordY() - b.coordY();
        double dz = a.coordZ() - b.coordZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Aplica la convolución SE(3)-invariante ponderada por distancias radiales (RBF).
     */
    public static double computeSe3MolecularEnergyScore(List<Protein3DAtomicEmbedding> atoms) {
        if (atoms == null || atoms.size() < 2) {
            return 0.0;
        }

        double totalEnergy = 0.0;
        for (int i = 0; i < atoms.size(); i++) {
            for (int j = i + 1; j < atoms.size(); j++) {
                double dist = computePairwiseDistance(atoms.get(i), atoms.get(j));
                if (dist > 1e-4) {
                    // Potencial de Lennard-Jones simplificado / Kernel radial
                    double r6 = Math.pow(1.5 / dist, 6);
                    totalEnergy += 4.0 * (r6 * r6 - r6);
                }
            }
        }
        return totalEnergy;
    }
}
