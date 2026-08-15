package com.corp.proyectosmartagrisupplychain.infrastructure.adapter.out.persistence;

import com.corp.proyectosmartagrisupplychain.domain.model.AgriSupplyTrack;
import com.corp.proyectosmartagrisupplychain.domain.port.out.AgriSupplyTrackRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAgriSupplyTrackRepositoryAdapter implements AgriSupplyTrackRepositoryPort {

    private final ConcurrentMap<String, AgriSupplyTrack> storage = new ConcurrentHashMap<>();

    @Override
    public AgriSupplyTrack save(AgriSupplyTrack entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AgriSupplyTrack> findById(String id, String tenantId) {
        AgriSupplyTrack entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
