package com.corp.proyectospaceisltelecommunications.infrastructure.adapter.out.persistence;

import com.corp.proyectospaceisltelecommunications.domain.model.SpaceSatelliteIslNode;
import com.corp.proyectospaceisltelecommunications.domain.port.out.SpaceSatelliteIslNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySpaceSatelliteIslNodeRepositoryAdapter implements SpaceSatelliteIslNodeRepositoryPort {

    private final ConcurrentMap<String, SpaceSatelliteIslNode> storage = new ConcurrentHashMap<>();

    @Override
    public SpaceSatelliteIslNode save(SpaceSatelliteIslNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpaceSatelliteIslNode> findById(String id, String tenantId) {
        SpaceSatelliteIslNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
