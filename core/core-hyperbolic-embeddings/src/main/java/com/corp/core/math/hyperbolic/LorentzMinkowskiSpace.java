package com.corp.core.math.hyperbolic;

import java.io.Serializable;

/**
 * Modelo hiperboloide de Lorentz-Minkowski con producto interno \(\langle x, y \rangle_L = -x_0 y_0 + \sum_{i=1}^n x_i y_i\).
 */
public record LorentzMinkowskiSpace() implements Serializable {

    public static double lorentzianInnerProduct(double[] x, double[] y) {
        if (x.length != y.length || x.length < 2) {
            throw new IllegalArgumentException("Dimensiones inválidas para espacio de Lorentz");
        }
        double prod = -x[0] * y[0];
        for (int i = 1; i < x.length; i++) {
            prod += x[i] * y[i];
        }
        return prod;
    }

    public static double lorentzDistance(double[] x, double[] y) {
        double inner = lorentzianInnerProduct(x, y);
        double arg = Math.max(1.0, -inner);
        return Math.log(arg + Math.sqrt(Math.max(0.0, arg * arg - 1.0)));
    }
}
