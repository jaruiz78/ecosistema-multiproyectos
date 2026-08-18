package com.corp.proyectofusionnuclearmhd.infrastructure.adapter.out.persistence;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import com.corp.proyectofusionnuclearmhd.domain.port.out.PlasmaConfinementZoneRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPlasmaConfinementZoneRepositoryAdapter implements PlasmaConfinementZoneRepositoryPort {

    private final ConcurrentMap<String, PlasmaConfinementZone> storage = new ConcurrentHashMap<>();

    @Override
    public PlasmaConfinementZone save(PlasmaConfinementZone entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PlasmaConfinementZone> findById(String id, String tenantId) {
        PlasmaConfinementZone entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
