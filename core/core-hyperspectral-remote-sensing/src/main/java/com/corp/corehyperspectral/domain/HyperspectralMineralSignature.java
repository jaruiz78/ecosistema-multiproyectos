package com.corp.corehyperspectral.domain;

import java.io.Serializable;

public record HyperspectralMineralSignature(
        String sceneId,
        long h3CellLocation,
        double[] fractionalAbundances,
        double soilCarbonIndex,
        boolean criticalMineralDepositFound
) implements Serializable {}
