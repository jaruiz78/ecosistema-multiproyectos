package com.corp.core.math.lbm;

import com.corp.corelbm.domain.LbmFluidVelocityField;

import java.io.Serializable;

/**
 * Motor de Dinámica de Fluidos por el Método de Lattice Boltzmann (LBM) en red D2Q9.
 * Implementa el operador de colisión BGK (Bhatnagar-Gross-Krook) y propagación en streaming.
 */
public class LatticeBoltzmannD2Q9Engine implements Serializable {

    // Pesos de la red D2Q9
    private static final double[] WEIGHTS = {
            4.0 / 9.0,
            1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0,
            1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0
    };

    // Vectores de velocidad discreta c_i
    private static final int[] CX = {0, 1, 0, -1, 0, 1, -1, -1, 1};
    private static final int[] CY = {0, 0, 1, 0, -1, 1, 1, -1, -1};

    /**
     * Calcula la función de distribución de equilibrio f_i^{eq} para una dirección dada.
     */
    public static double computeEquilibrium(int i, double rho, double ux, double uy) {
        double uDotC = CX[i] * ux + CY[i] * uy;
        double uSq = ux * ux + uy * uy;
        return WEIGHTS[i] * rho * (1.0 + 3.0 * uDotC + 4.5 * (uDotC * uDotC) - 1.5 * uSq);
    }

    /**
     * Aplica el paso de colisión BGK: f_i^* = f_i - (f_i - f_i^{eq}) / tau.
     */
    public static double collideBgk(double fi, double fiEq, double relaxationTau) {
        return fi - (fi - fiEq) / relaxationTau;
    }

    /**
     * Reconstruye las variables macroscópicas (densidad rho y velocidades ux, uy) a partir de las 9 distribuciones.
     */
    public static LbmFluidVelocityField computeMacroscopic(int x, int y, double[] f) {
        double rho = 0.0;
        double momentumX = 0.0;
        double momentumY = 0.0;

        for (int i = 0; i < 9; i++) {
            rho += f[i];
            momentumX += f[i] * CX[i];
            momentumY += f[i] * CY[i];
        }

        double ux = (rho > 0.0) ? momentumX / rho : 0.0;
        double uy = (rho > 0.0) ? momentumY / rho : 0.0;

        return LbmFluidVelocityField.create(x, y, rho, ux, uy);
    }
}
