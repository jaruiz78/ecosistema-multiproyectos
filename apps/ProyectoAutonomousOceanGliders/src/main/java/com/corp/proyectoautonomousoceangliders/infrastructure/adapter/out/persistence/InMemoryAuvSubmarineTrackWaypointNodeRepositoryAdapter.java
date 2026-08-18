package com.corp.proyectoautonomousoceangliders.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousoceangliders.domain.model.AuvSubmarineTrackWaypointNode;
import com.corp.proyectoautonomousoceangliders.domain.port.out.AuvSubmarineTrackWaypointNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAuvSubmarineTrackWaypointNodeRepositoryAdapter implements AuvSubmarineTrackWaypointNodeRepositoryPort {

    private final ConcurrentMap<String, AuvSubmarineTrackWaypointNode> storage = new ConcurrentHashMap<>();

    @Override
    public AuvSubmarineTrackWaypointNode save(AuvSubmarineTrackWaypointNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AuvSubmarineTrackWaypointNode> findById(String id, String tenantId) {
        AuvSubmarineTrackWaypointNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
