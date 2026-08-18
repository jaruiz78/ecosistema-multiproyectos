package com.corp.proyectosmartdestinationdti.infrastructure.adapter.out.persistence;

import com.corp.proyectosmartdestinationdti.domain.model.DestinationCapacityZone;
import com.corp.proyectosmartdestinationdti.domain.port.out.DestinationCapacityZoneRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDestinationCapacityZoneRepositoryAdapter implements DestinationCapacityZoneRepositoryPort {

    private final ConcurrentMap<String, DestinationCapacityZone> storage = new ConcurrentHashMap<>();

    @Override
    public DestinationCapacityZone save(DestinationCapacityZone entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DestinationCapacityZone> findById(String id, String tenantId) {
        DestinationCapacityZone entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
