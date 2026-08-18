package com.corp.proyectohypersonicintercontinentalfreight.infrastructure.adapter.out.persistence;

import com.corp.proyectohypersonicintercontinentalfreight.domain.model.ScramjetCombustionPressureRatioNode;
import com.corp.proyectohypersonicintercontinentalfreight.domain.port.out.ScramjetCombustionPressureRatioNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryScramjetCombustionPressureRatioNodeRepositoryAdapter implements ScramjetCombustionPressureRatioNodeRepositoryPort {

    private final ConcurrentMap<String, ScramjetCombustionPressureRatioNode> storage = new ConcurrentHashMap<>();

    @Override
    public ScramjetCombustionPressureRatioNode save(ScramjetCombustionPressureRatioNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ScramjetCombustionPressureRatioNode> findById(String id, String tenantId) {
        ScramjetCombustionPressureRatioNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
