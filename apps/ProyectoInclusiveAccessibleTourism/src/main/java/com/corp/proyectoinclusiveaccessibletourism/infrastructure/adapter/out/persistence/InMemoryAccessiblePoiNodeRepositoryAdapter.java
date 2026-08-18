package com.corp.proyectoinclusiveaccessibletourism.infrastructure.adapter.out.persistence;

import com.corp.proyectoinclusiveaccessibletourism.domain.model.AccessiblePoiNode;
import com.corp.proyectoinclusiveaccessibletourism.domain.port.out.AccessiblePoiNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAccessiblePoiNodeRepositoryAdapter implements AccessiblePoiNodeRepositoryPort {

    private final ConcurrentMap<String, AccessiblePoiNode> storage = new ConcurrentHashMap<>();

    @Override
    public AccessiblePoiNode save(AccessiblePoiNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AccessiblePoiNode> findById(String id, String tenantId) {
        AccessiblePoiNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
