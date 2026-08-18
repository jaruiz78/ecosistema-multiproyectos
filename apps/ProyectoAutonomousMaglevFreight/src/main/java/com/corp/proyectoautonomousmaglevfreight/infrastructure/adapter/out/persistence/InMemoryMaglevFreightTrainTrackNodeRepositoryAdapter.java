package com.corp.proyectoautonomousmaglevfreight.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousmaglevfreight.domain.model.MaglevFreightTrainTrackNode;
import com.corp.proyectoautonomousmaglevfreight.domain.port.out.MaglevFreightTrainTrackNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMaglevFreightTrainTrackNodeRepositoryAdapter implements MaglevFreightTrainTrackNodeRepositoryPort {

    private final ConcurrentMap<String, MaglevFreightTrainTrackNode> storage = new ConcurrentHashMap<>();

    @Override
    public MaglevFreightTrainTrackNode save(MaglevFreightTrainTrackNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MaglevFreightTrainTrackNode> findById(String id, String tenantId) {
        MaglevFreightTrainTrackNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
