package com.corp.core.math.symbolic;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Verificador formal en runtime de propiedades de Lógica Temporal Lineal (LTL):
 * - Always (\u25a1 P): La propiedad se cumple en todos los estados de la traza.
 * - Eventually (\u25c7 P): La propiedad se cumple en al menos un estado de la traza.
 * - Response (\u25a1(P -> \u25c7 Q)): Siempre que ocurra P, eventualmente ocurrirá Q.
 */
public record LtlFormulaVerifier() implements Serializable {

    public static <T> boolean verifyAlways(List<T> trace, Predicate<T> predicate) {
        if (trace == null || trace.isEmpty()) return false;
        return trace.stream().allMatch(predicate);
    }

    public static <T> boolean verifyEventually(List<T> trace, Predicate<T> predicate) {
        if (trace == null || trace.isEmpty()) return false;
        return trace.stream().anyMatch(predicate);
    }

    public static <T> boolean verifyResponse(List<T> trace, Predicate<T> stimulus, Predicate<T> response) {
        if (trace == null || trace.isEmpty()) return false;
        for (int i = 0; i < trace.size(); i++) {
            if (stimulus.test(trace.get(i))) {
                boolean responseFound = false;
                for (int j = i; j < trace.size(); j++) {
                    if (response.test(trace.get(j))) {
                        responseFound = true;
                        break;
                    }
                }
                if (!responseFound) return false;
            }
        }
        return true;
    }
}
