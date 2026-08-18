package com.corp.proyectosuperconductinggridprotection.infrastructure.adapter.out.persistence;

import com.corp.proyectosuperconductinggridprotection.domain.model.HtsFaultCurrentLimiterNode;
import com.corp.proyectosuperconductinggridprotection.domain.port.out.HtsFaultCurrentLimiterNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHtsFaultCurrentLimiterNodeRepositoryAdapter implements HtsFaultCurrentLimiterNodeRepositoryPort {

    private final ConcurrentMap<String, HtsFaultCurrentLimiterNode> storage = new ConcurrentHashMap<>();

    @Override
    public HtsFaultCurrentLimiterNode save(HtsFaultCurrentLimiterNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HtsFaultCurrentLimiterNode> findById(String id, String tenantId) {
        HtsFaultCurrentLimiterNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
