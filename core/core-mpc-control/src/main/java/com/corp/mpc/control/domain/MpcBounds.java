package com.corp.mpc.control.domain;

import java.util.Arrays;
import java.util.Objects;

/**
 * Restricciones físicas de caja (Box Constraints) para variables de control y estado.
 *
 * @param minBounds Límites inferiores admisibles
 * @param maxBounds Límites superiores admisibles
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 */
public record MpcBounds(
        double[] minBounds,
        double[] maxBounds
) {
    public MpcBounds {
        Objects.requireNonNull(minBounds, "minBounds no puede ser nulo");
        Objects.requireNonNull(maxBounds, "maxBounds no puede ser nulo");
        if (minBounds.length != maxBounds.length || minBounds.length == 0) {
            throw new IllegalArgumentException("minBounds y maxBounds deben tener la misma longitud positiva");
        }
        for (int i = 0; i < minBounds.length; i++) {
            if (minBounds[i] > maxBounds[i]) {
                throw new IllegalArgumentException("minBound no puede ser mayor que maxBound en el índice " + i);
            }
        }
        minBounds = Arrays.copyOf(minBounds, minBounds.length);
        maxBounds = Arrays.copyOf(maxBounds, maxBounds.length);
    }

    public int dimension() {
        return minBounds.length;
    }

    public double clamp(int index, double value) {
        return Math.max(minBounds[index], Math.min(maxBounds[index], value));
    }
}
