package com.corp.corelbm.application;

import com.corp.core.math.lbm.LatticeBoltzmannD2Q9Engine;
import com.corp.corelbm.domain.LbmFluidVelocityField;

import java.io.Serializable;

/**
 * Caso de uso para simular perfiles de flujo de fluidos y dispersión de partículas mediante LBM.
 */
public class LatticeBoltzmannFluidUseCase implements Serializable {

    public LbmFluidVelocityField simulateFluidNode(int x, int y, double initialRho, double initialUx, double initialUy, double tau) {
        double[] f = new double[9];
        for (int i = 0; i < 9; i++) {
            double feq = LatticeBoltzmannD2Q9Engine.computeEquilibrium(i, initialRho, initialUx, initialUy);
            f[i] = LatticeBoltzmannD2Q9Engine.collideBgk(feq, feq, tau);
        }
        return LatticeBoltzmannD2Q9Engine.computeMacroscopic(x, y, f);
    }
}
