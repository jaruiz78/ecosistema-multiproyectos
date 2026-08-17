package com.corp.corebft.domain;

import java.io.Serializable;

public record ConsensusRoundResult(
        String roundId,
        int totalNodes,
        String committedDigest,
        boolean finalized,
        int byzantineFaultsTolerated
) implements Serializable {}
