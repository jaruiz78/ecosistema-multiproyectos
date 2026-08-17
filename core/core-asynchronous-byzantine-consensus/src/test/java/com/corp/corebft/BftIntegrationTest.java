package com.corp.corebft;

import com.corp.core.math.bft.AsynchronousBftEngine;
import com.corp.corebft.application.AsynchronousBftExecutionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BftIntegrationTest {

    @Test
    @DisplayName("Debe ejecutar ronda aBFT y finalizar acuerdo de consenso tolerante a fallos")
    void testExecuteConsensusRoundIntegration() {
        AsynchronousBftExecutionUseCase useCase = new AsynchronousBftExecutionUseCase();
        var votes = List.of(
                new AsynchronousBftEngine.NodeVote("N1", "STATE_DIGEST_OK", true, false),
                new AsynchronousBftEngine.NodeVote("N2", "STATE_DIGEST_OK", true, false),
                new AsynchronousBftEngine.NodeVote("N3", "STATE_DIGEST_OK", true, false),
                new AsynchronousBftEngine.NodeVote("N4", "CORRUPT_DIGEST", true, false)
        );

        var result = useCase.executeConsensusRound("ROUND-2026-001", 4, votes);

        assertNotNull(result);
        assertTrue(result.finalized());
        assertEquals("STATE_DIGEST_OK", result.committedDigest());
        assertEquals(1, result.byzantineFaultsTolerated());
    }
}
