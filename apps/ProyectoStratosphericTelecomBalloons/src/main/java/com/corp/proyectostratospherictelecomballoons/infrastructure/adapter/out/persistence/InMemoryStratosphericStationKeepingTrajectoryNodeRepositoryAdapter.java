package com.corp.proyectostratospherictelecomballoons.infrastructure.adapter.out.persistence;

import com.corp.proyectostratospherictelecomballoons.domain.model.StratosphericStationKeepingTrajectoryNode;
import com.corp.proyectostratospherictelecomballoons.domain.port.out.StratosphericStationKeepingTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryStratosphericStationKeepingTrajectoryNodeRepositoryAdapter implements StratosphericStationKeepingTrajectoryNodeRepositoryPort {

    private final ConcurrentMap<String, StratosphericStationKeepingTrajectoryNode> storage = new ConcurrentHashMap<>();

    @Override
    public StratosphericStationKeepingTrajectoryNode save(StratosphericStationKeepingTrajectoryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<StratosphericStationKeepingTrajectoryNode> findById(String id, String tenantId) {
        StratosphericStationKeepingTrajectoryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
