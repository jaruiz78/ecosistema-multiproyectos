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
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ConfidentialGovTechBftAuditService implements Serializable {

    private final ConfidentialEnclaveHost enclaveHost = new ConfidentialEnclaveHost();
    private final AsynchronousBftExecutionUseCase bftUseCase = new AsynchronousBftExecutionUseCase();

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
