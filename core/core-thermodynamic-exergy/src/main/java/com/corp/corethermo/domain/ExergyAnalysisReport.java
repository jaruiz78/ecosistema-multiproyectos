package com.corp.corethermo.domain;

import java.io.Serializable;

public record ExergyAnalysisReport(
        String plantId,
        double physicalExergyInKw,
        double exergyDestroyedKw,
        double secondLawEfficiencyPct,
        boolean optimalOperation
) implements Serializable {}
