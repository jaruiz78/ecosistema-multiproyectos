package com.corp.proyectoastroturismostarlight.infrastructure.adapter.out.persistence;

import com.corp.proyectoastroturismostarlight.domain.model.StarlightObservationPoint;
import com.corp.proyectoastroturismostarlight.domain.port.out.StarlightObservationPointRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryStarlightObservationPointRepositoryAdapter implements StarlightObservationPointRepositoryPort {

    private final ConcurrentMap<String, StarlightObservationPoint> storage = new ConcurrentHashMap<>();

    @Override
    public StarlightObservationPoint save(StarlightObservationPoint entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<StarlightObservationPoint> findById(String id, String tenantId) {
        StarlightObservationPoint entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
