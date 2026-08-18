package com.corp.proyectodeepspacesolarsailmission.infrastructure.adapter.out.persistence;

import com.corp.proyectodeepspacesolarsailmission.domain.model.SolarSailAttitudeVectorNode;
import com.corp.proyectodeepspacesolarsailmission.domain.port.out.SolarSailAttitudeVectorNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySolarSailAttitudeVectorNodeRepositoryAdapter implements SolarSailAttitudeVectorNodeRepositoryPort {

    private final ConcurrentMap<String, SolarSailAttitudeVectorNode> storage = new ConcurrentHashMap<>();

    @Override
    public SolarSailAttitudeVectorNode save(SolarSailAttitudeVectorNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SolarSailAttitudeVectorNode> findById(String id, String tenantId) {
        SolarSailAttitudeVectorNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
