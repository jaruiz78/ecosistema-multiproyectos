package com.corp.coresymbolic.application;

import com.corp.core.math.symbolic.LtlFormulaVerifier;
import com.corp.coresymbolic.domain.SymbolicVerificationState;

import java.util.List;
import java.util.function.Predicate;

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
