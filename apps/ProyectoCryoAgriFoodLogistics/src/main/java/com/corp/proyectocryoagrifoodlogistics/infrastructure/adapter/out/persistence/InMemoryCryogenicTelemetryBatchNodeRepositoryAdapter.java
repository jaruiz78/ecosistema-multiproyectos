package com.corp.proyectocryoagrifoodlogistics.infrastructure.adapter.out.persistence;

import com.corp.proyectocryoagrifoodlogistics.domain.model.CryogenicTelemetryBatchNode;
import com.corp.proyectocryoagrifoodlogistics.domain.port.out.CryogenicTelemetryBatchNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCryogenicTelemetryBatchNodeRepositoryAdapter implements CryogenicTelemetryBatchNodeRepositoryPort {

    private final ConcurrentMap<String, CryogenicTelemetryBatchNode> storage = new ConcurrentHashMap<>();

    @Override
    public CryogenicTelemetryBatchNode save(CryogenicTelemetryBatchNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CryogenicTelemetryBatchNode> findById(String id, String tenantId) {
        CryogenicTelemetryBatchNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
