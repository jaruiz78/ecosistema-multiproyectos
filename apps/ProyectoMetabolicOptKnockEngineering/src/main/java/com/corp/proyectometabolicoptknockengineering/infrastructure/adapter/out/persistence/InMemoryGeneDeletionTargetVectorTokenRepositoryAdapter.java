package com.corp.proyectometabolicoptknockengineering.infrastructure.adapter.out.persistence;

import com.corp.proyectometabolicoptknockengineering.domain.model.GeneDeletionTargetVectorToken;
import com.corp.proyectometabolicoptknockengineering.domain.port.out.GeneDeletionTargetVectorTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryGeneDeletionTargetVectorTokenRepositoryAdapter implements GeneDeletionTargetVectorTokenRepositoryPort {

    private final ConcurrentMap<String, GeneDeletionTargetVectorToken> storage = new ConcurrentHashMap<>();

    @Override
    public GeneDeletionTargetVectorToken save(GeneDeletionTargetVectorToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GeneDeletionTargetVectorToken> findById(String id, String tenantId) {
        GeneDeletionTargetVectorToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
