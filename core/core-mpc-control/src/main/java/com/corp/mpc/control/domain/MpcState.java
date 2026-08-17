package com.corp.mpc.control.domain;

import java.util.Arrays;
import java.util.Objects;

/**
 * Estado dinámico de un sistema bajo control predictivo (MPC).
 * Java 25 Record inmutable.
 *
 * @param stateVector Vector de variables de estado (ej. niveles de embalse, SoC baterías, presiones)
 * @param timestampMs Marca temporal en milisegundos
 * @param tenantId    Identificador de partición multi-tenant
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 * @see docs/adr/adr-001-java25-virtual-threads-anti-pinning.md
 */
public record MpcState(
        double[] stateVector,
        long timestampMs,
        String tenantId
) {
    public MpcState {
        Objects.requireNonNull(stateVector, "stateVector no puede ser nulo");
        Objects.requireNonNull(tenantId, "tenantId no puede ser nulo");
        if (stateVector.length == 0) {
            throw new IllegalArgumentException("stateVector debe tener al menos una dimensión");
        }
        if (timestampMs < 0) {
            throw new IllegalArgumentException("timestampMs no puede ser negativo");
        }
        // Copia defensiva inmutable
        stateVector = Arrays.copyOf(stateVector, stateVector.length);
    }

    public int dimension() {
        return stateVector.length;
    }
}
