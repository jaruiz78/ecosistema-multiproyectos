package com.corp.proyectohyporheicwaterpurification.infrastructure.adapter.out.persistence;

import com.corp.proyectohyporheicwaterpurification.domain.model.HyporheicNitrateDenitrificationNode;
import com.corp.proyectohyporheicwaterpurification.domain.port.out.HyporheicNitrateDenitrificationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHyporheicNitrateDenitrificationNodeRepositoryAdapter implements HyporheicNitrateDenitrificationNodeRepositoryPort {

    private final ConcurrentMap<String, HyporheicNitrateDenitrificationNode> storage = new ConcurrentHashMap<>();

    @Override
    public HyporheicNitrateDenitrificationNode save(HyporheicNitrateDenitrificationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HyporheicNitrateDenitrificationNode> findById(String id, String tenantId) {
        HyporheicNitrateDenitrificationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
