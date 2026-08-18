package com.corp.proyectocoralreefecosystempreserve.infrastructure.adapter.out.persistence;

import com.corp.proyectocoralreefecosystempreserve.domain.model.CoralBleachingDegreeWeekNode;
import com.corp.proyectocoralreefecosystempreserve.domain.port.out.CoralBleachingDegreeWeekNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCoralBleachingDegreeWeekNodeRepositoryAdapter implements CoralBleachingDegreeWeekNodeRepositoryPort {

    private final ConcurrentMap<String, CoralBleachingDegreeWeekNode> storage = new ConcurrentHashMap<>();

    @Override
    public CoralBleachingDegreeWeekNode save(CoralBleachingDegreeWeekNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CoralBleachingDegreeWeekNode> findById(String id, String tenantId) {
        CoralBleachingDegreeWeekNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
