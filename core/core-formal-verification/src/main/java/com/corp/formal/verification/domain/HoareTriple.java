package com.corp.formal.verification.domain;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Terna de Hoare Formal: {P} C {Q}
 *
 * <p>Donde:
 * <ul>
 *   <li>{@code P}: Precondición que debe satisfacer el estado previo</li>
 *   <li>{@code C}: Comando o transición de estado determinista {@code state -> nextState}</li>
 *   <li>{@code Q}: Poscondición o relación relacional entre el estado inicial y el final</li>
 * </ul>
 *
 * @param name          Identificador formal del contrato
 * @param precondition  Predicado de precondición P
 * @param command       Función de transición de estado C
 * @param postcondition Predicado relacional de poscondición Q (estadoInicial, estadoFinal) -> boolean
 * @param <S>           Tipo del estado
 *
 * @see docs/formacion_ecosistema/modulo_1_sistemas_distribuidos_concurrencia/01_fundamentos_lamport_raft.md
 */
public record HoareTriple<S>(
        String name,
        Predicate<S> precondition,
        Function<S, S> command,
        BiPredicate<S, S> postcondition
) {
    public HoareTriple {
        Objects.requireNonNull(name, "name no puede ser nulo");
        Objects.requireNonNull(precondition, "precondition no puede ser nula");
        Objects.requireNonNull(command, "command no puede ser nulo");
        Objects.requireNonNull(postcondition, "postcondition no puede ser nula");
    }
}
