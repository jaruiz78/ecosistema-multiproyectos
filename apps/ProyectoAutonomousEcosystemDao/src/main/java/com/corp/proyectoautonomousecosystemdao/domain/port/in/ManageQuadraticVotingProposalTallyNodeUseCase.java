package com.corp.proyectoautonomousecosystemdao.domain.port.in;

import com.corp.proyectoautonomousecosystemdao.domain.model.QuadraticVotingProposalTallyNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQuadraticVotingProposalTallyNodeUseCase {
    QuadraticVotingProposalTallyNode createQuadraticVotingProposalTallyNode(String tenantId, String title, double value);
    Optional<QuadraticVotingProposalTallyNode> findQuadraticVotingProposalTallyNodeById(String id, String tenantId);
    QuadraticVotingProposalTallyNode processOptimization(String id, String tenantId);
}
