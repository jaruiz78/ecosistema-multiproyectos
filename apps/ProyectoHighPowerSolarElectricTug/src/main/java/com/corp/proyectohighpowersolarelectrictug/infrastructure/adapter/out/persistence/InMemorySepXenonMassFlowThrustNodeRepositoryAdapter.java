package com.corp.proyectohighpowersolarelectrictug.infrastructure.adapter.out.persistence;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import com.corp.proyectohighpowersolarelectrictug.domain.port.out.SepXenonMassFlowThrustNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySepXenonMassFlowThrustNodeRepositoryAdapter implements SepXenonMassFlowThrustNodeRepositoryPort {

    private final ConcurrentMap<String, SepXenonMassFlowThrustNode> storage = new ConcurrentHashMap<>();

    @Override
    public SepXenonMassFlowThrustNode save(SepXenonMassFlowThrustNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SepXenonMassFlowThrustNode> findById(String id, String tenantId) {
        SepXenonMassFlowThrustNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
