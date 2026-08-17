package com.corp.proyectob2g.application;

import com.corp.core.math.bft.AsynchronousBftEngine;
import com.corp.proyectob2g.application.service.ConfidentialGovTechBftAuditService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfidentialGovTechBftAuditServiceTest {

    @Test
    @DisplayName("Debe auditar pliego público en enclave SGX y validar con consenso aBFT supermayoritario")
    void testConfidentialGovTechBftSynergy() {
        var service = new ConfidentialGovTechBftAuditService();

        var votes = List.of(
                new AsynchronousBftEngine.NodeVote("MINISTERIO_HACIENDA", "DIGEST_TENDER_AWARDED", true, false),
                new AsynchronousBftEngine.NodeVote("INTERVENCION_GENERAL", "DIGEST_TENDER_AWARDED", true, false),
                new AsynchronousBftEngine.NodeVote("TRIBUNAL_CUENTAS", "DIGEST_TENDER_AWARDED", true, false),
                new AsynchronousBftEngine.NodeVote("OBSERVADOR_EXTERNO", "CORRUPTED_DIGEST", true, false)
        );

        var result = service.auditPublicTenderConfidential(
                "TENDER-INFRA-2026-99", "BINARY_SEALED_BIDS_DATA", 4, votes
        );

        assertNotNull(result);
        assertEquals("TENDER-INFRA-2026-99", result.tenderId());
        assertTrue(result.auditProofVerified());
        assertTrue(result.legallyFinalized());
        assertEquals("DIGEST_TENDER_AWARDED", result.committedConsensusDigest());
    }
}
