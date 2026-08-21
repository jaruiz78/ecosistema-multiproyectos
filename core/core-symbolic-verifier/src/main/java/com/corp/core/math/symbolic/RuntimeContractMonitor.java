package com.corp.core.math.symbolic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Monitor determinista de invariantes en runtime con semántica de autómata de Büchi simplificado.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record RuntimeContractMonitor<T>(
        String contractId,
        List<T> stateHistory,
        boolean violationDetected,
        String lastViolationReason
) implements Serializable {

    public static <T> RuntimeContractMonitor<T> create(String contractId) {
        return new RuntimeContractMonitor<>(contractId, Collections.emptyList(), false, null);
    }

    public RuntimeContractMonitor<T> appendState(T state, java.util.function.Predicate<T> safetyInvariant, String invariantName) {
        if (violationDetected) {
            return this;
        }
        List<T> newHistory = new ArrayList<>(stateHistory);
        newHistory.add(state);

        if (!safetyInvariant.test(state)) {
            return new RuntimeContractMonitor<>(contractId, Collections.unmodifiableList(newHistory), true, "Violación de invariante: " + invariantName);
        }

        return new RuntimeContractMonitor<>(contractId, Collections.unmodifiableList(newHistory), false, null);
    }
}
