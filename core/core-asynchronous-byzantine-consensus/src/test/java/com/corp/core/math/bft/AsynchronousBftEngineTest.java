package com.corp.core.math.bft;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AsynchronousBftEngineTest {

    @Test
    @DisplayName("Debe alcanzar supermayoría y finalizar bloque con f < n/3 nodos bizantinos")
    void testThresholdAgreementSuccess() {
        int n = 4; // n=4, f=1, supermayoría = 2*1 + 1 = 3
        var votes = List.of(
                new AsynchronousBftEngine.NodeVote("NODE_1", "BLOCK_HASH_A", true, false),
                new AsynchronousBftEngine.NodeVote("NODE_2", "BLOCK_HASH_A", true, false),
                new AsynchronousBftEngine.NodeVote("NODE_3", "BLOCK_HASH_A", true, false),
                new AsynchronousBftEngine.NodeVote("NODE_4", "BLOCK_HASH_B", true, true) // Bizantino
        );

        var decision = AsynchronousBftEngine.evaluateThresholdAgreement(votes, n);

        assertTrue(decision.consensusAchieved());
        assertEquals("BLOCK_HASH_A", decision.agreedDigest());
        assertEquals(3, decision.validVotesCount());
    }

    @Test
    @DisplayName("Debe validar anclaje de transacción en grafo DAG-Tangle")
    void testValidateDagAttachment() {
        var tx = new DagTangleValidator.TangleTransaction("TX-100", List.of("TX-98", "TX-99"), 10L);
        assertTrue(DagTangleValidator.validateDagAttachment(tx));
    }
}
