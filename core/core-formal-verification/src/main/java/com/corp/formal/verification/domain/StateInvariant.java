package com.corp.formal.verification.domain;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Invariante formal de estado: Representa una propiedad lógica o cota que debe satisfacerse
 * de forma inductiva en todo estado válido del sistema.
 *
 * @param name        Identificador semántico del invariante (ej: "Conservación de Energía", "No-Sobrecarga")
 * @param description Justificación formal y referencia matemática
 * @param predicate   Función de evaluación determinista sobre el estado genérico {@code T}
 * @param <T>         Tipo del estado bajo verificación
 *
 * @see docs/formacion_ecosistema/modulo_1_sistemas_distribuidos_concurrencia/01_fundamentos_lamport_raft.md
 */
public record StateInvariant<T>(
        String name,
        String description,
        Predicate<T> predicate
) {
    public StateInvariant {
        Objects.requireNonNull(name, "name no puede ser nulo");
        Objects.requireNonNull(description, "description no puede ser nula");
        Objects.requireNonNull(predicate, "predicate no puede ser nulo");
    }

    /**
     * Evalúa si el estado satisface el invariante.
     */
    public boolean evaluate(T state) {
        if (state == null) return false;
        try {
            return predicate.test(state);
        } catch (Exception e) {
            return false;
        }
    }
}
