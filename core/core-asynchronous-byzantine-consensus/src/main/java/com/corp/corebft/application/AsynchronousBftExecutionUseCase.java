package com.corp.corebft.application;

import com.corp.core.math.bft.AsynchronousBftEngine;
import com.corp.corebft.domain.ConsensusRoundResult;

import java.util.List;

public class AsynchronousBftExecutionUseCase {

    public ConsensusRoundResult executeConsensusRound(String roundId, int networkSize, List<AsynchronousBftEngine.NodeVote> votes) {
        var decision = AsynchronousBftEngine.evaluateThresholdAgreement(votes, networkSize);
        int maxFaulty = (networkSize - 1) / 3;

        return new ConsensusRoundResult(
                roundId,
                networkSize,
                decision.agreedDigest(),
                decision.consensusAchieved(),
                maxFaulty
        );
    }
}
