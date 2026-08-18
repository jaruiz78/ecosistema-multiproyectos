package com.corp.proyectographenefastchargestorage.infrastructure.adapter.out.persistence;

import com.corp.proyectographenefastchargestorage.domain.model.GrapheneSupercapacitorCellBatch;
import com.corp.proyectographenefastchargestorage.domain.port.out.GrapheneSupercapacitorCellBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryGrapheneSupercapacitorCellBatchRepositoryAdapter implements GrapheneSupercapacitorCellBatchRepositoryPort {

    private final ConcurrentMap<String, GrapheneSupercapacitorCellBatch> storage = new ConcurrentHashMap<>();

    @Override
    public GrapheneSupercapacitorCellBatch save(GrapheneSupercapacitorCellBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GrapheneSupercapacitorCellBatch> findById(String id, String tenantId) {
        GrapheneSupercapacitorCellBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
