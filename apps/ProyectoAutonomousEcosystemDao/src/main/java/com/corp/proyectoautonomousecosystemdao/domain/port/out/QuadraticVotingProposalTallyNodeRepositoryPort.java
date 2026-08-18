package com.corp.proyectoautonomousecosystemdao.domain.port.out;

import com.corp.proyectoautonomousecosystemdao.domain.model.QuadraticVotingProposalTallyNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QuadraticVotingProposalTallyNodeRepositoryPort {
    QuadraticVotingProposalTallyNode save(QuadraticVotingProposalTallyNode entity);
    Optional<QuadraticVotingProposalTallyNode> findById(String id, String tenantId);
}
