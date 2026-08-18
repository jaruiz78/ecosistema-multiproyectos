package com.corp.proyectoorbitaldebrislaserdeflector.infrastructure.adapter.out.persistence;

import com.corp.proyectoorbitaldebrislaserdeflector.domain.model.LaserAblationImpulseDeltaVToken;
import com.corp.proyectoorbitaldebrislaserdeflector.domain.port.out.LaserAblationImpulseDeltaVTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryLaserAblationImpulseDeltaVTokenRepositoryAdapter implements LaserAblationImpulseDeltaVTokenRepositoryPort {

    private final ConcurrentMap<String, LaserAblationImpulseDeltaVToken> storage = new ConcurrentHashMap<>();

    @Override
    public LaserAblationImpulseDeltaVToken save(LaserAblationImpulseDeltaVToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LaserAblationImpulseDeltaVToken> findById(String id, String tenantId) {
        LaserAblationImpulseDeltaVToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
