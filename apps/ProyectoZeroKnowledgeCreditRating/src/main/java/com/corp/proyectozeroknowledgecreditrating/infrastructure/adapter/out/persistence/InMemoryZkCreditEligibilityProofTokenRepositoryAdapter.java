package com.corp.proyectozeroknowledgecreditrating.infrastructure.adapter.out.persistence;

import com.corp.proyectozeroknowledgecreditrating.domain.model.ZkCreditEligibilityProofToken;
import com.corp.proyectozeroknowledgecreditrating.domain.port.out.ZkCreditEligibilityProofTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryZkCreditEligibilityProofTokenRepositoryAdapter implements ZkCreditEligibilityProofTokenRepositoryPort {

    private final ConcurrentMap<String, ZkCreditEligibilityProofToken> storage = new ConcurrentHashMap<>();

    @Override
    public ZkCreditEligibilityProofToken save(ZkCreditEligibilityProofToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ZkCreditEligibilityProofToken> findById(String id, String tenantId) {
        ZkCreditEligibilityProofToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
