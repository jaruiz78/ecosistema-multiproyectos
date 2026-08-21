package com.corp.coresymbolic.application;

import com.corp.core.math.symbolic.LtlFormulaVerifier;
import com.corp.coresymbolic.domain.SymbolicVerificationState;

import java.util.List;
import java.util.function.Predicate;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SymbolicModelCheckingUseCase {

    public <T> SymbolicVerificationState verifySafetyTrace(String traceId, List<T> trace, Predicate<T> safetyInvariant) {
        boolean valid = LtlFormulaVerifier.verifyAlways(trace, safetyInvariant);
        return new SymbolicVerificationState(
                traceId,
                trace != null ? trace.size() : 0,
                valid,
                valid ? "Propiedad LTL Always satisfecha" : "Violación de seguridad detectada en traza"
        );
    }
}
