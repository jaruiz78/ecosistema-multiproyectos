package com.corp.coresymbolic.domain;

import java.io.Serializable;

public record SymbolicVerificationState(
        String traceId,
        int totalStatesEvaluated,
        boolean valid,
        String summary
) implements Serializable {}
