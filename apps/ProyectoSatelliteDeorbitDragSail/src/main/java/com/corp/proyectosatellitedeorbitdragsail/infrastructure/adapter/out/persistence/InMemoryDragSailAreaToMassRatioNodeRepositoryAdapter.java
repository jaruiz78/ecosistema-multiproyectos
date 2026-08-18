package com.corp.proyectosatellitedeorbitdragsail.infrastructure.adapter.out.persistence;

import com.corp.proyectosatellitedeorbitdragsail.domain.model.DragSailAreaToMassRatioNode;
import com.corp.proyectosatellitedeorbitdragsail.domain.port.out.DragSailAreaToMassRatioNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDragSailAreaToMassRatioNodeRepositoryAdapter implements DragSailAreaToMassRatioNodeRepositoryPort {

    private final ConcurrentMap<String, DragSailAreaToMassRatioNode> storage = new ConcurrentHashMap<>();

    @Override
    public DragSailAreaToMassRatioNode save(DragSailAreaToMassRatioNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DragSailAreaToMassRatioNode> findById(String id, String tenantId) {
        DragSailAreaToMassRatioNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
