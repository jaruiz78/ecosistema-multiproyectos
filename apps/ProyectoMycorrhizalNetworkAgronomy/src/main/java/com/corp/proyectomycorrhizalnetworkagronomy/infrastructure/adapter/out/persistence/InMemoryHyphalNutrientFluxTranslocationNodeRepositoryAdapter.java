package com.corp.proyectomycorrhizalnetworkagronomy.infrastructure.adapter.out.persistence;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import com.corp.proyectomycorrhizalnetworkagronomy.domain.port.out.HyphalNutrientFluxTranslocationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHyphalNutrientFluxTranslocationNodeRepositoryAdapter implements HyphalNutrientFluxTranslocationNodeRepositoryPort {

    private final ConcurrentMap<String, HyphalNutrientFluxTranslocationNode> storage = new ConcurrentHashMap<>();

    @Override
    public HyphalNutrientFluxTranslocationNode save(HyphalNutrientFluxTranslocationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HyphalNutrientFluxTranslocationNode> findById(String id, String tenantId) {
        HyphalNutrientFluxTranslocationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
