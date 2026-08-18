package com.corp.proyectoreusablerocketlandingtwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoreusablerocketlandingtwin.domain.model.RocketRetroLandingTrajectoryNode;
import com.corp.proyectoreusablerocketlandingtwin.domain.port.out.RocketRetroLandingTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryRocketRetroLandingTrajectoryNodeRepositoryAdapter implements RocketRetroLandingTrajectoryNodeRepositoryPort {

    private final ConcurrentMap<String, RocketRetroLandingTrajectoryNode> storage = new ConcurrentHashMap<>();

    @Override
    public RocketRetroLandingTrajectoryNode save(RocketRetroLandingTrajectoryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<RocketRetroLandingTrajectoryNode> findById(String id, String tenantId) {
        RocketRetroLandingTrajectoryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
