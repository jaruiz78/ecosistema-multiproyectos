package com.corp.proyectoorganonachippharmascreen.infrastructure.adapter.out.persistence;

import com.corp.proyectoorganonachippharmascreen.domain.model.MicrofluidicPerfusionChannelNode;
import com.corp.proyectoorganonachippharmascreen.domain.port.out.MicrofluidicPerfusionChannelNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMicrofluidicPerfusionChannelNodeRepositoryAdapter implements MicrofluidicPerfusionChannelNodeRepositoryPort {

    private final ConcurrentMap<String, MicrofluidicPerfusionChannelNode> storage = new ConcurrentHashMap<>();

    @Override
    public MicrofluidicPerfusionChannelNode save(MicrofluidicPerfusionChannelNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MicrofluidicPerfusionChannelNode> findById(String id, String tenantId) {
        MicrofluidicPerfusionChannelNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
