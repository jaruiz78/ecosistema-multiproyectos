package com.corp.coresynbio.domain;

import java.io.Serializable;

public record GeneExpressionProfile(
        String circuitId,
        String hostOrganism,
        double inputAConcentrationUm,
        double inputBConcentrationUm,
        double proteinOutputRpu, // Relative Promoter Units
        boolean logicStateHigh
) implements Serializable {}
