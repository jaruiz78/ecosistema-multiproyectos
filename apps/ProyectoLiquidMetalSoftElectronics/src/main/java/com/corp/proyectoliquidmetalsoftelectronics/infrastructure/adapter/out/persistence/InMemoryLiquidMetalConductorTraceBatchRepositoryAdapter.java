package com.corp.proyectoliquidmetalsoftelectronics.infrastructure.adapter.out.persistence;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.out.LiquidMetalConductorTraceBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryLiquidMetalConductorTraceBatchRepositoryAdapter implements LiquidMetalConductorTraceBatchRepositoryPort {

    private final ConcurrentMap<String, LiquidMetalConductorTraceBatch> storage = new ConcurrentHashMap<>();

    @Override
    public LiquidMetalConductorTraceBatch save(LiquidMetalConductorTraceBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LiquidMetalConductorTraceBatch> findById(String id, String tenantId) {
        LiquidMetalConductorTraceBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
