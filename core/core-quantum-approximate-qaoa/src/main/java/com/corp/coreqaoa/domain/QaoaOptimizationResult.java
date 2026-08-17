package com.corp.coreqaoa.domain;

import java.io.Serializable;

public record QaoaOptimizationResult(
        String graphId,
        int numNodes,
        int[] optimalSpinPartition,
        double groundStateEnergy,
        boolean quantumSpeedupAchieved
) implements Serializable {}
