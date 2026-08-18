package com.corp.proyectocgraspatialaccelerator.infrastructure.adapter.out.persistence;

import com.corp.proyectocgraspatialaccelerator.domain.model.CgraComputeMeshTileNode;
import com.corp.proyectocgraspatialaccelerator.domain.port.out.CgraComputeMeshTileNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCgraComputeMeshTileNodeRepositoryAdapter implements CgraComputeMeshTileNodeRepositoryPort {

    private final ConcurrentMap<String, CgraComputeMeshTileNode> storage = new ConcurrentHashMap<>();

    @Override
    public CgraComputeMeshTileNode save(CgraComputeMeshTileNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CgraComputeMeshTileNode> findById(String id, String tenantId) {
        CgraComputeMeshTileNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
