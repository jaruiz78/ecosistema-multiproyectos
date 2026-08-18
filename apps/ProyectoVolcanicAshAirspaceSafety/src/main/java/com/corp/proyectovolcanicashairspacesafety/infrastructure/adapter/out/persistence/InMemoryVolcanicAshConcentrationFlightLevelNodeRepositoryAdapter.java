package com.corp.proyectovolcanicashairspacesafety.infrastructure.adapter.out.persistence;

import com.corp.proyectovolcanicashairspacesafety.domain.model.VolcanicAshConcentrationFlightLevelNode;
import com.corp.proyectovolcanicashairspacesafety.domain.port.out.VolcanicAshConcentrationFlightLevelNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryVolcanicAshConcentrationFlightLevelNodeRepositoryAdapter implements VolcanicAshConcentrationFlightLevelNodeRepositoryPort {

    private final ConcurrentMap<String, VolcanicAshConcentrationFlightLevelNode> storage = new ConcurrentHashMap<>();

    @Override
    public VolcanicAshConcentrationFlightLevelNode save(VolcanicAshConcentrationFlightLevelNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VolcanicAshConcentrationFlightLevelNode> findById(String id, String tenantId) {
        VolcanicAshConcentrationFlightLevelNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
