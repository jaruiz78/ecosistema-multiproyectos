package com.corp.proyectomantlegeodynamicssimulator.infrastructure.adapter.out.persistence;

import com.corp.proyectomantlegeodynamicssimulator.domain.model.MantlePlumeThermalUpwellingNode;
import com.corp.proyectomantlegeodynamicssimulator.domain.port.out.MantlePlumeThermalUpwellingNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMantlePlumeThermalUpwellingNodeRepositoryAdapter implements MantlePlumeThermalUpwellingNodeRepositoryPort {

    private final ConcurrentMap<String, MantlePlumeThermalUpwellingNode> storage = new ConcurrentHashMap<>();

    @Override
    public MantlePlumeThermalUpwellingNode save(MantlePlumeThermalUpwellingNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MantlePlumeThermalUpwellingNode> findById(String id, String tenantId) {
        MantlePlumeThermalUpwellingNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
