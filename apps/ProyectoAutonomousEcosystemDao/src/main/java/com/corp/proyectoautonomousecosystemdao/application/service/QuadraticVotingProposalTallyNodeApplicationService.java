package com.corp.proyectoautonomousecosystemdao.application.service;

import com.corp.proyectoautonomousecosystemdao.domain.model.QuadraticVotingProposalTallyNode;
import com.corp.proyectoautonomousecosystemdao.domain.port.in.ManageQuadraticVotingProposalTallyNodeUseCase;
import com.corp.proyectoautonomousecosystemdao.domain.port.out.QuadraticVotingProposalTallyNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuadraticVotingProposalTallyNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuadraticVotingProposalTallyNodeApplicationService implements ManageQuadraticVotingProposalTallyNodeUseCase {

    private final QuadraticVotingProposalTallyNodeRepositoryPort repositoryPort;

    public QuadraticVotingProposalTallyNodeApplicationService(QuadraticVotingProposalTallyNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuadraticVotingProposalTallyNode createQuadraticVotingProposalTallyNode(String tenantId, String title, double value) {
        QuadraticVotingProposalTallyNode entity = new QuadraticVotingProposalTallyNode(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<QuadraticVotingProposalTallyNode> findQuadraticVotingProposalTallyNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuadraticVotingProposalTallyNode processOptimization(String id, String tenantId) {
        QuadraticVotingProposalTallyNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuadraticVotingProposalTallyNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
