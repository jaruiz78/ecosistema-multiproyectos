package com.corp.proyectohydrogenfuelcelllongrangedrone.infrastructure.adapter.out.persistence;

import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.model.PemFuelCellStackEfficiencyNode;
import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.port.out.PemFuelCellStackEfficiencyNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPemFuelCellStackEfficiencyNodeRepositoryAdapter implements PemFuelCellStackEfficiencyNodeRepositoryPort {

    private final ConcurrentMap<String, PemFuelCellStackEfficiencyNode> storage = new ConcurrentHashMap<>();

    @Override
    public PemFuelCellStackEfficiencyNode save(PemFuelCellStackEfficiencyNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PemFuelCellStackEfficiencyNode> findById(String id, String tenantId) {
        PemFuelCellStackEfficiencyNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
