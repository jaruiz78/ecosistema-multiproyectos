package com.corp.proyectoautonomousecosystemdao.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousecosystemdao.domain.model.QuadraticVotingProposalTallyNode;
import com.corp.proyectoautonomousecosystemdao.domain.port.out.QuadraticVotingProposalTallyNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
