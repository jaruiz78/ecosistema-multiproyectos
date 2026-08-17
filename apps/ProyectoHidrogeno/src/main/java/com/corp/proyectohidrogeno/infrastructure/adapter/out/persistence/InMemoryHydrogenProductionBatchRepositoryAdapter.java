package com.corp.proyectohidrogeno.infrastructure.adapter.out.persistence;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import com.corp.proyectohidrogeno.domain.port.out.HydrogenProductionBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHydrogenProductionBatchRepositoryAdapter implements HydrogenProductionBatchRepositoryPort {

    private final ConcurrentMap<String, HydrogenProductionBatch> storage = new ConcurrentHashMap<>();

    @Override
    public HydrogenProductionBatch save(HydrogenProductionBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HydrogenProductionBatch> findById(String id, String tenantId) {
        HydrogenProductionBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
