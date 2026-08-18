package com.corp.proyectoplayasinteligentescostas.infrastructure.adapter.out.persistence;

import com.corp.proyectoplayasinteligentescostas.domain.model.BeachSectorSafetyZone;
import com.corp.proyectoplayasinteligentescostas.domain.port.out.BeachSectorSafetyZoneRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBeachSectorSafetyZoneRepositoryAdapter implements BeachSectorSafetyZoneRepositoryPort {

    private final ConcurrentMap<String, BeachSectorSafetyZone> storage = new ConcurrentHashMap<>();

    @Override
    public BeachSectorSafetyZone save(BeachSectorSafetyZone entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BeachSectorSafetyZone> findById(String id, String tenantId) {
        BeachSectorSafetyZone entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
