package com.corp.corehyperbolic.application;

import com.corp.core.math.hyperbolic.PoincareDiskModel;
import com.corp.corehyperbolic.domain.HyperbolicPoint;

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
