package com.corp.proyectowildfirefrontpredictor.infrastructure.adapter.out.persistence;

import com.corp.proyectowildfirefrontpredictor.domain.model.WildfirePropagationFrontNode;
import com.corp.proyectowildfirefrontpredictor.domain.port.out.WildfirePropagationFrontNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryWildfirePropagationFrontNodeRepositoryAdapter implements WildfirePropagationFrontNodeRepositoryPort {

    private final ConcurrentMap<String, WildfirePropagationFrontNode> storage = new ConcurrentHashMap<>();

    @Override
    public WildfirePropagationFrontNode save(WildfirePropagationFrontNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<WildfirePropagationFrontNode> findById(String id, String tenantId) {
        WildfirePropagationFrontNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
