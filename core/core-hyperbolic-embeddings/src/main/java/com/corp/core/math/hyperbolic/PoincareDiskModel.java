package com.corp.core.math.hyperbolic;

import java.io.Serializable;

/**
 * Modelo de Disco de Poincaré \(\mathbb{D}^n = \{x \in \mathbb{R}^n : \|x\| < 1\}\)
 * con métrica conforme \(g_x = \left(\frac{2}{1 - \|x\|^2}\right)^2 g_E\).
 *
 * La distancia hiperbólica entre dos puntos \(u, v \in \mathbb{D}^n\) es:
 * \[
 * d_{\mathbb{D}}(u, v) = \text{arcosh}\left(1 + 2 \frac{\|u - v\|^2}{(1 - \|u\|^2)(1 - \|v\|^2)}\right)
 * \]
 */
public record PoincareDiskModel() implements Serializable {

    public static double distance(double[] u, double[] v) {
        if (u.length != v.length) {
            throw new IllegalArgumentException("Dimensiones incompatibles");
        }

        double normUSq = normSq(u);
        double normVSq = normSq(v);

        if (normUSq >= 1.0 || normVSq >= 1.0) {
            throw new IllegalArgumentException("Puntos deben residir estrictamente dentro del disco unitario abierto");
        }

        double diffSq = 0.0;
        for (int i = 0; i < u.length; i++) {
            double d = u[i] - v[i];
            diffSq += d * d;
        }

        double alpha = 1.0 - normUSq;
        double beta = 1.0 - normVSq;
        double arg = 1.0 + 2.0 * diffSq / (alpha * beta);

        // arcosh(x) = ln(x + sqrt(x^2 - 1))
        return Math.log(arg + Math.sqrt(Math.max(0.0, arg * arg - 1.0)));
    }

    public static double normSq(double[] x) {
        double sum = 0.0;
        for (double v : x) {
            sum += v * v;
        }
        return sum;
    }
}
