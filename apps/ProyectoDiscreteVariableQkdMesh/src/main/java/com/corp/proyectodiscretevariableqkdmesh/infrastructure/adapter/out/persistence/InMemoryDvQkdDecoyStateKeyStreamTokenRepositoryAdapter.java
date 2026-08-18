package com.corp.proyectodiscretevariableqkdmesh.infrastructure.adapter.out.persistence;

import com.corp.proyectodiscretevariableqkdmesh.domain.model.DvQkdDecoyStateKeyStreamToken;
import com.corp.proyectodiscretevariableqkdmesh.domain.port.out.DvQkdDecoyStateKeyStreamTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDvQkdDecoyStateKeyStreamTokenRepositoryAdapter implements DvQkdDecoyStateKeyStreamTokenRepositoryPort {

    private final ConcurrentMap<String, DvQkdDecoyStateKeyStreamToken> storage = new ConcurrentHashMap<>();

    @Override
    public DvQkdDecoyStateKeyStreamToken save(DvQkdDecoyStateKeyStreamToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DvQkdDecoyStateKeyStreamToken> findById(String id, String tenantId) {
        DvQkdDecoyStateKeyStreamToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
