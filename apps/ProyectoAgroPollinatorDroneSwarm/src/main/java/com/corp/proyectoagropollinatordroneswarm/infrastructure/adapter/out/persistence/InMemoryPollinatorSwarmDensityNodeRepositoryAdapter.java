package com.corp.proyectoagropollinatordroneswarm.infrastructure.adapter.out.persistence;

import com.corp.proyectoagropollinatordroneswarm.domain.model.PollinatorSwarmDensityNode;
import com.corp.proyectoagropollinatordroneswarm.domain.port.out.PollinatorSwarmDensityNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPollinatorSwarmDensityNodeRepositoryAdapter implements PollinatorSwarmDensityNodeRepositoryPort {

    private final ConcurrentMap<String, PollinatorSwarmDensityNode> storage = new ConcurrentHashMap<>();

    @Override
    public PollinatorSwarmDensityNode save(PollinatorSwarmDensityNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PollinatorSwarmDensityNode> findById(String id, String tenantId) {
        PollinatorSwarmDensityNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
