package com.corp.core.math.bft;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Motor de consenso bizantino asíncrono (aBFT) tolerante a \(f < n/3\) nodos maliciosos
 * basado en intercambio de umbral de votos (Threshold Byzantine Agreement).
 */
public record AsynchronousBftEngine() implements Serializable {

    public record NodeVote(
            String nodeId,
            String proposedBlockDigest,
            boolean signatureValid,
            boolean isByzantine
    ) implements Serializable {}

    public record ConsensusDecision(
            boolean consensusAchieved,
            String agreedDigest,
            int totalVotes,
            int supermajorityThreshold,
            int validVotesCount
    ) implements Serializable {}

    public static ConsensusDecision evaluateThresholdAgreement(List<NodeVote> votes, int totalNetworkNodes) {
        int maxFaulty = (totalNetworkNodes - 1) / 3;
        int supermajority = 2 * maxFaulty + 1;

        if (votes == null || votes.isEmpty()) {
            return new ConsensusDecision(false, null, 0, supermajority, 0);
        }

        // Filtrar votos con firma válida y no bizantinos
        Map<String, Long> digestCounts = votes.stream()
                .filter(NodeVote::signatureValid)
                .filter(v -> !v.isByzantine())
                .collect(Collectors.groupingBy(NodeVote::proposedBlockDigest, Collectors.counting()));

        for (Map.Entry<String, Long> entry : digestCounts.entrySet()) {
            if (entry.getValue() >= supermajority) {
                return new ConsensusDecision(true, entry.getKey(), votes.size(), supermajority, entry.getValue().intValue());
            }
        }

        return new ConsensusDecision(false, null, votes.size(), supermajority, 0);
    }
}
