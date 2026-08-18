package com.corp.proyectoplanetaryaerocapturemission.infrastructure.adapter.out.persistence;

import com.corp.proyectoplanetaryaerocapturemission.domain.model.AerocapturePeakHeatFluxTrajectoryNode;
import com.corp.proyectoplanetaryaerocapturemission.domain.port.out.AerocapturePeakHeatFluxTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAerocapturePeakHeatFluxTrajectoryNodeRepositoryAdapter implements AerocapturePeakHeatFluxTrajectoryNodeRepositoryPort {

    private final ConcurrentMap<String, AerocapturePeakHeatFluxTrajectoryNode> storage = new ConcurrentHashMap<>();

    @Override
    public AerocapturePeakHeatFluxTrajectoryNode save(AerocapturePeakHeatFluxTrajectoryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AerocapturePeakHeatFluxTrajectoryNode> findById(String id, String tenantId) {
        AerocapturePeakHeatFluxTrajectoryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
