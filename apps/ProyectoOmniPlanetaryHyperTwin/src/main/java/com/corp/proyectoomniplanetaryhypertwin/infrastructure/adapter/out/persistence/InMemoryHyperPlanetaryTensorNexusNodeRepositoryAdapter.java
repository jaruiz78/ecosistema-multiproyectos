package com.corp.proyectoomniplanetaryhypertwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoomniplanetaryhypertwin.domain.model.HyperPlanetaryTensorNexusNode;
import com.corp.proyectoomniplanetaryhypertwin.domain.port.out.HyperPlanetaryTensorNexusNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHyperPlanetaryTensorNexusNodeRepositoryAdapter implements HyperPlanetaryTensorNexusNodeRepositoryPort {

    private final ConcurrentMap<String, HyperPlanetaryTensorNexusNode> storage = new ConcurrentHashMap<>();

    @Override
    public HyperPlanetaryTensorNexusNode save(HyperPlanetaryTensorNexusNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HyperPlanetaryTensorNexusNode> findById(String id, String tenantId) {
        HyperPlanetaryTensorNexusNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
