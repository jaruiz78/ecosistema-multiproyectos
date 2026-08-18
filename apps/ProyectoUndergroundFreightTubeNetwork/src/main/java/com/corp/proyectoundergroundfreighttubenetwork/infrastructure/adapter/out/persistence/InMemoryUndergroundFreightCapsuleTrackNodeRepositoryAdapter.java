package com.corp.proyectoundergroundfreighttubenetwork.infrastructure.adapter.out.persistence;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import com.corp.proyectoundergroundfreighttubenetwork.domain.port.out.UndergroundFreightCapsuleTrackNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUndergroundFreightCapsuleTrackNodeRepositoryAdapter implements UndergroundFreightCapsuleTrackNodeRepositoryPort {

    private final ConcurrentMap<String, UndergroundFreightCapsuleTrackNode> storage = new ConcurrentHashMap<>();

    @Override
    public UndergroundFreightCapsuleTrackNode save(UndergroundFreightCapsuleTrackNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<UndergroundFreightCapsuleTrackNode> findById(String id, String tenantId) {
        UndergroundFreightCapsuleTrackNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
