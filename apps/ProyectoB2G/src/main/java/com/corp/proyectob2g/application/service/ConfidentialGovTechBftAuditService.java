package com.corp.proyectob2g.application.service;

import com.corp.core.math.bft.AsynchronousBftEngine;
import com.corp.corebft.application.AsynchronousBftExecutionUseCase;
import com.corp.corebft.domain.ConsensusRoundResult;
import com.corp.starter.enclave.ConfidentialEnclaveHost;

import java.io.Serializable;
import java.util.List;

/**
 * Servicio de orquestación sinérgica GovTech que ejecuta la evaluación de licitaciones públicas
 * dentro de enclaves confidenciales de hardware SGX/SEV y ratifica el resultado mediante consenso bizantino asíncrono aBFT.
 */
public class ConfidentialGovTechBftAuditService implements Serializable {

    private final ConfidentialEnclaveHost enclaveHost = new ConfidentialEnclaveHost();
    private final AsynchronousBftExecutionUseCase bftUseCase = new AsynchronousBftExecutionUseCase();

    public record ConfidentialTenderAuditResult(
            String tenderId,
            String enclaveQuoteMeasurement,
            String committedConsensusDigest,
            boolean auditProofVerified,
            boolean legallyFinalized
    ) implements Serializable {}

    public ConfidentialTenderAuditResult auditPublicTenderConfidential(
            String tenderId,
            String tenderBinaryPayload,
            int totalAdminNodes,
            List<AsynchronousBftEngine.NodeVote> adminVotes
    ) {
        // 1. Evaluar pliego dentro del enclave seguro de hardware
        var quote = enclaveHost.generateRemoteQuote(tenderBinaryPayload);
        boolean quoteValid = enclaveHost.verifyEnclaveQuote(quote);

        // 2. Ejecutar consenso aBFT entre las administraciones públicas participantes
        ConsensusRoundResult bftResult = bftUseCase.executeConsensusRound(
                "TENDER-ROUND-" + tenderId, totalAdminNodes, adminVotes
        );

        boolean finalized = quoteValid && bftResult.finalized();

        return new ConfidentialTenderAuditResult(
                tenderId,
                quote.enclaveMeasurementMrEnclave(),
                bftResult.committedDigest(),
                quoteValid,
                finalized
        );
    }
}
