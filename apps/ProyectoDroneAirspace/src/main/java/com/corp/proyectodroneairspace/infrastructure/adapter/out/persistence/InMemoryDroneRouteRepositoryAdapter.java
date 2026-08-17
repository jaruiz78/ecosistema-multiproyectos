package com.corp.proyectodroneairspace.infrastructure.adapter.out.persistence;

import com.corp.proyectodroneairspace.domain.model.DroneRoute;
import com.corp.proyectodroneairspace.domain.port.out.DroneRouteRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDroneRouteRepositoryAdapter implements DroneRouteRepositoryPort {

    private final ConcurrentMap<String, DroneRoute> storage = new ConcurrentHashMap<>();

    @Override
    public DroneRoute save(DroneRoute entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DroneRoute> findById(String id, String tenantId) {
        DroneRoute entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
