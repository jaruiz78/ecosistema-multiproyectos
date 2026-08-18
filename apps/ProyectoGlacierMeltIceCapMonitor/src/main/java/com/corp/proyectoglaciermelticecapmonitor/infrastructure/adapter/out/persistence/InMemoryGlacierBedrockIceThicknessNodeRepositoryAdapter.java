package com.corp.proyectoglaciermelticecapmonitor.infrastructure.adapter.out.persistence;

import com.corp.proyectoglaciermelticecapmonitor.domain.model.GlacierBedrockIceThicknessNode;
import com.corp.proyectoglaciermelticecapmonitor.domain.port.out.GlacierBedrockIceThicknessNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryGlacierBedrockIceThicknessNodeRepositoryAdapter implements GlacierBedrockIceThicknessNodeRepositoryPort {

    private final ConcurrentMap<String, GlacierBedrockIceThicknessNode> storage = new ConcurrentHashMap<>();

    @Override
    public GlacierBedrockIceThicknessNode save(GlacierBedrockIceThicknessNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GlacierBedrockIceThicknessNode> findById(String id, String tenantId) {
        GlacierBedrockIceThicknessNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
