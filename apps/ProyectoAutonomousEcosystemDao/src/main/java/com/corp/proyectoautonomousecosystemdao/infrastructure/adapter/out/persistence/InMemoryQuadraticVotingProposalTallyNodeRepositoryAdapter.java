package com.corp.proyectoautonomousecosystemdao.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousecosystemdao.domain.model.QuadraticVotingProposalTallyNode;
import com.corp.proyectoautonomousecosystemdao.domain.port.out.QuadraticVotingProposalTallyNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryQuadraticVotingProposalTallyNodeRepositoryAdapter implements QuadraticVotingProposalTallyNodeRepositoryPort {

    private final ConcurrentMap<String, QuadraticVotingProposalTallyNode> storage = new ConcurrentHashMap<>();

    @Override
    public QuadraticVotingProposalTallyNode save(QuadraticVotingProposalTallyNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QuadraticVotingProposalTallyNode> findById(String id, String tenantId) {
        QuadraticVotingProposalTallyNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
