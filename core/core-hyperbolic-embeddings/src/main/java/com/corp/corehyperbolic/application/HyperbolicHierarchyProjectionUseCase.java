package com.corp.corehyperbolic.application;

import com.corp.core.math.hyperbolic.PoincareDiskModel;
import com.corp.corehyperbolic.domain.HyperbolicPoint;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HyperbolicHierarchyProjectionUseCase {

    public double calculateHierarchyDistance(HyperbolicPoint parent, HyperbolicPoint child) {
        return PoincareDiskModel.distance(parent.coordinates(), child.coordinates());
    }

    public HyperbolicPoint projectChild(String childId, HyperbolicPoint parent, double radialOffset) {
        double[] parentCoords = parent.coordinates();
        double[] childCoords = new double[parentCoords.length];
        for (int i = 0; i < parentCoords.length; i++) {
            childCoords[i] = parentCoords[i] * (1.0 - radialOffset * 0.1) + 0.05 * radialOffset;
        }
        return new HyperbolicPoint(childId, childCoords, parent.curvatureRadius());
    }
}
