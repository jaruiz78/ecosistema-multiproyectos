package com.corp.proyectolitertedgeinferencehub.infrastructure.adapter.out.persistence;

import com.corp.proyectolitertedgeinferencehub.domain.model.LiteRtQuantizedModelExecutionNode;
import com.corp.proyectolitertedgeinferencehub.domain.port.out.LiteRtQuantizedModelExecutionNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryLiteRtQuantizedModelExecutionNodeRepositoryAdapter implements LiteRtQuantizedModelExecutionNodeRepositoryPort {

    private final ConcurrentMap<String, LiteRtQuantizedModelExecutionNode> storage = new ConcurrentHashMap<>();

    @Override
    public LiteRtQuantizedModelExecutionNode save(LiteRtQuantizedModelExecutionNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LiteRtQuantizedModelExecutionNode> findById(String id, String tenantId) {
        LiteRtQuantizedModelExecutionNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
