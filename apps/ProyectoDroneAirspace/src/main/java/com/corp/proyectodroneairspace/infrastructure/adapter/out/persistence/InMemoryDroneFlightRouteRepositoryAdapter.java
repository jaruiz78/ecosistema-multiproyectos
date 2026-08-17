package com.corp.proyectodroneairspace.infrastructure.adapter.out.persistence;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import com.corp.proyectodroneairspace.domain.port.out.DroneFlightRouteRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDroneFlightRouteRepositoryAdapter implements DroneFlightRouteRepositoryPort {

    private final ConcurrentMap<String, DroneFlightRoute> storage = new ConcurrentHashMap<>();

    @Override
    public DroneFlightRoute save(DroneFlightRoute entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DroneFlightRoute> findById(String id, String tenantId) {
        DroneFlightRoute entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
