package com.corp.proyectoagrifoodcoldchaintrace.infrastructure.adapter.out.persistence;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import com.corp.proyectoagrifoodcoldchaintrace.domain.port.out.ColdChainShipmentBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryColdChainShipmentBatchRepositoryAdapter implements ColdChainShipmentBatchRepositoryPort {

    private final ConcurrentMap<String, ColdChainShipmentBatch> storage = new ConcurrentHashMap<>();

    @Override
    public ColdChainShipmentBatch save(ColdChainShipmentBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ColdChainShipmentBatch> findById(String id, String tenantId) {
        ColdChainShipmentBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
