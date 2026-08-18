package com.corp.proyectosubmarinevolcanomonitoring.infrastructure.adapter.out.persistence;

import com.corp.proyectosubmarinevolcanomonitoring.domain.model.VolcanicHydroacousticSeismicNode;
import com.corp.proyectosubmarinevolcanomonitoring.domain.port.out.VolcanicHydroacousticSeismicNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryVolcanicHydroacousticSeismicNodeRepositoryAdapter implements VolcanicHydroacousticSeismicNodeRepositoryPort {

    private final ConcurrentMap<String, VolcanicHydroacousticSeismicNode> storage = new ConcurrentHashMap<>();

    @Override
    public VolcanicHydroacousticSeismicNode save(VolcanicHydroacousticSeismicNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VolcanicHydroacousticSeismicNode> findById(String id, String tenantId) {
        VolcanicHydroacousticSeismicNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
