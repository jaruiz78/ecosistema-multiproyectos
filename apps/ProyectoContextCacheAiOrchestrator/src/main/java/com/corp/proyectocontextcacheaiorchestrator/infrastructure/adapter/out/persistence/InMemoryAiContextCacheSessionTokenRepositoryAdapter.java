package com.corp.proyectocontextcacheaiorchestrator.infrastructure.adapter.out.persistence;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.out.AiContextCacheSessionTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAiContextCacheSessionTokenRepositoryAdapter implements AiContextCacheSessionTokenRepositoryPort {

    private final ConcurrentMap<String, AiContextCacheSessionToken> storage = new ConcurrentHashMap<>();

    @Override
    public AiContextCacheSessionToken save(AiContextCacheSessionToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AiContextCacheSessionToken> findById(String id, String tenantId) {
        AiContextCacheSessionToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
