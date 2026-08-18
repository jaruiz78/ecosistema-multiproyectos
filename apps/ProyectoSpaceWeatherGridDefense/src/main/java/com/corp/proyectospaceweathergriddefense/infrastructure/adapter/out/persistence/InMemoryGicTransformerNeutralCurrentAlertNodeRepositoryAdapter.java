package com.corp.proyectospaceweathergriddefense.infrastructure.adapter.out.persistence;

import com.corp.proyectospaceweathergriddefense.domain.model.GicTransformerNeutralCurrentAlertNode;
import com.corp.proyectospaceweathergriddefense.domain.port.out.GicTransformerNeutralCurrentAlertNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryGicTransformerNeutralCurrentAlertNodeRepositoryAdapter implements GicTransformerNeutralCurrentAlertNodeRepositoryPort {

    private final ConcurrentMap<String, GicTransformerNeutralCurrentAlertNode> storage = new ConcurrentHashMap<>();

    @Override
    public GicTransformerNeutralCurrentAlertNode save(GicTransformerNeutralCurrentAlertNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GicTransformerNeutralCurrentAlertNode> findById(String id, String tenantId) {
        GicTransformerNeutralCurrentAlertNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
