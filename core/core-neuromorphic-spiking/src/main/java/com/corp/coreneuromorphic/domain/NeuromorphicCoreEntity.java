package com.corp.coreneuromorphic.domain;

import java.io.Serializable;

public record NeuromorphicCoreEntity(
        String networkId,
        int totalNeurons,
        double activeSpikeRateHz,
        boolean energyOptimal
) implements Serializable {}
