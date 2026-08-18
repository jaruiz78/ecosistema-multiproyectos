package com.corp.proyectolightningflashnowcastinggrid.infrastructure.adapter.out.persistence;

import com.corp.proyectolightningflashnowcastinggrid.domain.model.LightningFlashRateDensityNode;
import com.corp.proyectolightningflashnowcastinggrid.domain.port.out.LightningFlashRateDensityNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryLightningFlashRateDensityNodeRepositoryAdapter implements LightningFlashRateDensityNodeRepositoryPort {

    private final ConcurrentMap<String, LightningFlashRateDensityNode> storage = new ConcurrentHashMap<>();

    @Override
    public LightningFlashRateDensityNode save(LightningFlashRateDensityNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LightningFlashRateDensityNode> findById(String id, String tenantId) {
        LightningFlashRateDensityNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
