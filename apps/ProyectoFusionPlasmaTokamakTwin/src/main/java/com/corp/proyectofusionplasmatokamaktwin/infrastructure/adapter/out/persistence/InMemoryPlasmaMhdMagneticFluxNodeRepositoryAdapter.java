package com.corp.proyectofusionplasmatokamaktwin.infrastructure.adapter.out.persistence;

import com.corp.proyectofusionplasmatokamaktwin.domain.model.PlasmaMhdMagneticFluxNode;
import com.corp.proyectofusionplasmatokamaktwin.domain.port.out.PlasmaMhdMagneticFluxNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPlasmaMhdMagneticFluxNodeRepositoryAdapter implements PlasmaMhdMagneticFluxNodeRepositoryPort {

    private final ConcurrentMap<String, PlasmaMhdMagneticFluxNode> storage = new ConcurrentHashMap<>();

    @Override
    public PlasmaMhdMagneticFluxNode save(PlasmaMhdMagneticFluxNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PlasmaMhdMagneticFluxNode> findById(String id, String tenantId) {
        PlasmaMhdMagneticFluxNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
