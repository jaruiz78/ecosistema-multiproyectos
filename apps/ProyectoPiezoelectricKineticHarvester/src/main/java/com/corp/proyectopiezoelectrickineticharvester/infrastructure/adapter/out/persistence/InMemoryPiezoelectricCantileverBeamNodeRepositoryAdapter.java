package com.corp.proyectopiezoelectrickineticharvester.infrastructure.adapter.out.persistence;

import com.corp.proyectopiezoelectrickineticharvester.domain.model.PiezoelectricCantileverBeamNode;
import com.corp.proyectopiezoelectrickineticharvester.domain.port.out.PiezoelectricCantileverBeamNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPiezoelectricCantileverBeamNodeRepositoryAdapter implements PiezoelectricCantileverBeamNodeRepositoryPort {

    private final ConcurrentMap<String, PiezoelectricCantileverBeamNode> storage = new ConcurrentHashMap<>();

    @Override
    public PiezoelectricCantileverBeamNode save(PiezoelectricCantileverBeamNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PiezoelectricCantileverBeamNode> findById(String id, String tenantId) {
        PiezoelectricCantileverBeamNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
