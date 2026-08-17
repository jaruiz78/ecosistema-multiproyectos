package com.corp.mpc.control.domain;

import java.util.Arrays;
import java.util.Objects;

/**
 * Acción óptima calculada por el optimizador MPC para el siguiente paso de control.
 *
 * @param optimalControl Inputs de control calculados (u_0, u_1, ..., u_m)
 * @param totalCost      Valor evaluado de la función de coste cuadrático J
 * @param iterations     Número de iteraciones del solver
 * @param converged      Indica si el solver convergió dentro de la tolerancia de primal/dual residual
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 */
public record MpcControlAction(
        double[] optimalControl,
        double totalCost,
        int iterations,
        boolean converged
) {
    public MpcControlAction {
        Objects.requireNonNull(optimalControl, "optimalControl no puede ser nulo");
        optimalControl = Arrays.copyOf(optimalControl, optimalControl.length);
    }
}
