package com.corp.proyectostratosphericsai.infrastructure.adapter.out.persistence;

import com.corp.proyectostratosphericsai.domain.model.AerosolInjectionPlume;
import com.corp.proyectostratosphericsai.domain.port.out.AerosolInjectionPlumeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAerosolInjectionPlumeRepositoryAdapter implements AerosolInjectionPlumeRepositoryPort {

    private final ConcurrentMap<String, AerosolInjectionPlume> storage = new ConcurrentHashMap<>();

    @Override
    public AerosolInjectionPlume save(AerosolInjectionPlume entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AerosolInjectionPlume> findById(String id, String tenantId) {
        AerosolInjectionPlume entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
