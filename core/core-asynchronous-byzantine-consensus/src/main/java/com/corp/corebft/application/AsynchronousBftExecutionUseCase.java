package com.corp.corebft.application;

import com.corp.core.math.bft.AsynchronousBftEngine;
import com.corp.corebft.domain.ConsensusRoundResult;

import java.util.List;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
