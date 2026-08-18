package com.corp.proyectogreenhydrogendesal.infrastructure.adapter.out.persistence;

import com.corp.proyectogreenhydrogendesal.domain.model.DesalinationElectrolyzerUnit;
import com.corp.proyectogreenhydrogendesal.domain.port.out.DesalinationElectrolyzerUnitRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDesalinationElectrolyzerUnitRepositoryAdapter implements DesalinationElectrolyzerUnitRepositoryPort {

    private final ConcurrentMap<String, DesalinationElectrolyzerUnit> storage = new ConcurrentHashMap<>();

    @Override
    public DesalinationElectrolyzerUnit save(DesalinationElectrolyzerUnit entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DesalinationElectrolyzerUnit> findById(String id, String tenantId) {
        DesalinationElectrolyzerUnit entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
