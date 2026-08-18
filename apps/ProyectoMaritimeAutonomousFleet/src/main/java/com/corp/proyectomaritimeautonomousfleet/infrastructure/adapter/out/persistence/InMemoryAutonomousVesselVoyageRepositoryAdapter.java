package com.corp.proyectomaritimeautonomousfleet.infrastructure.adapter.out.persistence;

import com.corp.proyectomaritimeautonomousfleet.domain.model.AutonomousVesselVoyage;
import com.corp.proyectomaritimeautonomousfleet.domain.port.out.AutonomousVesselVoyageRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAutonomousVesselVoyageRepositoryAdapter implements AutonomousVesselVoyageRepositoryPort {

    private final ConcurrentMap<String, AutonomousVesselVoyage> storage = new ConcurrentHashMap<>();

    @Override
    public AutonomousVesselVoyage save(AutonomousVesselVoyage entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AutonomousVesselVoyage> findById(String id, String tenantId) {
        AutonomousVesselVoyage entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
