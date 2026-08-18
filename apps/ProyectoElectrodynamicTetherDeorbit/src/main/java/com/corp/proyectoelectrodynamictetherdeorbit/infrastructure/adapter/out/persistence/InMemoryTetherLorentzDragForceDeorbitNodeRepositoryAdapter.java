package com.corp.proyectoelectrodynamictetherdeorbit.infrastructure.adapter.out.persistence;

import com.corp.proyectoelectrodynamictetherdeorbit.domain.model.TetherLorentzDragForceDeorbitNode;
import com.corp.proyectoelectrodynamictetherdeorbit.domain.port.out.TetherLorentzDragForceDeorbitNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryTetherLorentzDragForceDeorbitNodeRepositoryAdapter implements TetherLorentzDragForceDeorbitNodeRepositoryPort {

    private final ConcurrentMap<String, TetherLorentzDragForceDeorbitNode> storage = new ConcurrentHashMap<>();

    @Override
    public TetherLorentzDragForceDeorbitNode save(TetherLorentzDragForceDeorbitNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TetherLorentzDragForceDeorbitNode> findById(String id, String tenantId) {
        TetherLorentzDragForceDeorbitNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
