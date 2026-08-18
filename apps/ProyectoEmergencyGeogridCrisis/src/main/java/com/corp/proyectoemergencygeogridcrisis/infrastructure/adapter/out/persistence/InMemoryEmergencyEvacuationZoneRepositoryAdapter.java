package com.corp.proyectoemergencygeogridcrisis.infrastructure.adapter.out.persistence;

import com.corp.proyectoemergencygeogridcrisis.domain.model.EmergencyEvacuationZone;
import com.corp.proyectoemergencygeogridcrisis.domain.port.out.EmergencyEvacuationZoneRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEmergencyEvacuationZoneRepositoryAdapter implements EmergencyEvacuationZoneRepositoryPort {

    private final ConcurrentMap<String, EmergencyEvacuationZone> storage = new ConcurrentHashMap<>();

    @Override
    public EmergencyEvacuationZone save(EmergencyEvacuationZone entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EmergencyEvacuationZone> findById(String id, String tenantId) {
        EmergencyEvacuationZone entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
