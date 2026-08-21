package com.corp.corelbm.domain;

import java.io.Serializable;

/**
 * Representa el campo macroscópico de densidad y velocidad de un fluido en una celda de la red LBM.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
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
