package com.corp.proyectohidrogeno.infrastructure.adapter.out.persistence;

import com.corp.proyectohidrogeno.domain.model.ElectrolyzerUnit;
import com.corp.proyectohidrogeno.domain.port.out.ElectrolyzerUnitRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryElectrolyzerUnitRepositoryAdapter implements ElectrolyzerUnitRepositoryPort {

    private final ConcurrentMap<String, ElectrolyzerUnit> storage = new ConcurrentHashMap<>();

    @Override
    public ElectrolyzerUnit save(ElectrolyzerUnit entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ElectrolyzerUnit> findById(String id, String tenantId) {
        ElectrolyzerUnit entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
