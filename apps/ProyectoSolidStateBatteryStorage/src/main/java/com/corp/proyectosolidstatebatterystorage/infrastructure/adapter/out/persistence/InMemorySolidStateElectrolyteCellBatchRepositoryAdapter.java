package com.corp.proyectosolidstatebatterystorage.infrastructure.adapter.out.persistence;

import com.corp.proyectosolidstatebatterystorage.domain.model.SolidStateElectrolyteCellBatch;
import com.corp.proyectosolidstatebatterystorage.domain.port.out.SolidStateElectrolyteCellBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySolidStateElectrolyteCellBatchRepositoryAdapter implements SolidStateElectrolyteCellBatchRepositoryPort {

    private final ConcurrentMap<String, SolidStateElectrolyteCellBatch> storage = new ConcurrentHashMap<>();

    @Override
    public SolidStateElectrolyteCellBatch save(SolidStateElectrolyteCellBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SolidStateElectrolyteCellBatch> findById(String id, String tenantId) {
        SolidStateElectrolyteCellBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
