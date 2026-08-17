package com.corp.corelbm.domain;

import java.io.Serializable;

/**
 * Representa el campo macroscópico de densidad y velocidad de un fluido en una celda de la red LBM.
 */
public record LbmFluidVelocityField(
        int gridX,
        int gridY,
        double densityRho,
        double velocityUx,
        double velocityUy,
        double vorticityOmega
) implements Serializable {

    public static LbmFluidVelocityField create(int x, int y, double rho, double ux, double uy) {
        double vorticity = Math.sqrt(ux * ux + uy * uy);
        return new LbmFluidVelocityField(x, y, rho, ux, uy, vorticity);
    }
}
