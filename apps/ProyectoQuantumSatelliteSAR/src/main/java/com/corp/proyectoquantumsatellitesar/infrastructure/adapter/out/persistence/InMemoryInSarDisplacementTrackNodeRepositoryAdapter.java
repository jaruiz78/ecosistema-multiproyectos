package com.corp.proyectoquantumsatellitesar.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumsatellitesar.domain.model.InSarDisplacementTrackNode;
import com.corp.proyectoquantumsatellitesar.domain.port.out.InSarDisplacementTrackNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryInSarDisplacementTrackNodeRepositoryAdapter implements InSarDisplacementTrackNodeRepositoryPort {

    private final ConcurrentMap<String, InSarDisplacementTrackNode> storage = new ConcurrentHashMap<>();

    @Override
    public InSarDisplacementTrackNode save(InSarDisplacementTrackNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<InSarDisplacementTrackNode> findById(String id, String tenantId) {
        InSarDisplacementTrackNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
