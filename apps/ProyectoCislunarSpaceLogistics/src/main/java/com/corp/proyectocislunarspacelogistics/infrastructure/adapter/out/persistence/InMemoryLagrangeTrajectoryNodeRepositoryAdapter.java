package com.corp.proyectocislunarspacelogistics.infrastructure.adapter.out.persistence;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTrajectoryNode;
import com.corp.proyectocislunarspacelogistics.domain.port.out.LagrangeTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryLagrangeTrajectoryNodeRepositoryAdapter implements LagrangeTrajectoryNodeRepositoryPort {

    private final ConcurrentMap<String, LagrangeTrajectoryNode> storage = new ConcurrentHashMap<>();

    @Override
    public LagrangeTrajectoryNode save(LagrangeTrajectoryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LagrangeTrajectoryNode> findById(String id, String tenantId) {
        LagrangeTrajectoryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
