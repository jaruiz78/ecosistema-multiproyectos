package com.corp.proyectoatmosphericwaterharvesting.infrastructure.adapter.out.persistence;

import com.corp.proyectoatmosphericwaterharvesting.domain.model.MofWaterAdsorptionChamberNode;
import com.corp.proyectoatmosphericwaterharvesting.domain.port.out.MofWaterAdsorptionChamberNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMofWaterAdsorptionChamberNodeRepositoryAdapter implements MofWaterAdsorptionChamberNodeRepositoryPort {

    private final ConcurrentMap<String, MofWaterAdsorptionChamberNode> storage = new ConcurrentHashMap<>();

    @Override
    public MofWaterAdsorptionChamberNode save(MofWaterAdsorptionChamberNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MofWaterAdsorptionChamberNode> findById(String id, String tenantId) {
        MofWaterAdsorptionChamberNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
