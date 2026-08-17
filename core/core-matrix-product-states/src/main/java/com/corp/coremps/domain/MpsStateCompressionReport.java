package com.corp.coremps.domain;

import java.io.Serializable;

public record MpsStateCompressionReport(
        String stateId,
        int totalQubits,
        int bondDimensionChi,
        double uncompressedParametersCount,
        double compressedParametersCount,
        double compressionRatioPct,
        boolean fidelityPreserved
) implements Serializable {}
