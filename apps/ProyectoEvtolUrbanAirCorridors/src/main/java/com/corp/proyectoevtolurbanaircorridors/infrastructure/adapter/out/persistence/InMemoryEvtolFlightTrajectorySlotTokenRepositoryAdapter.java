package com.corp.proyectoevtolurbanaircorridors.infrastructure.adapter.out.persistence;

import com.corp.proyectoevtolurbanaircorridors.domain.model.EvtolFlightTrajectorySlotToken;
import com.corp.proyectoevtolurbanaircorridors.domain.port.out.EvtolFlightTrajectorySlotTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEvtolFlightTrajectorySlotTokenRepositoryAdapter implements EvtolFlightTrajectorySlotTokenRepositoryPort {

    private final ConcurrentMap<String, EvtolFlightTrajectorySlotToken> storage = new ConcurrentHashMap<>();

    @Override
    public EvtolFlightTrajectorySlotToken save(EvtolFlightTrajectorySlotToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EvtolFlightTrajectorySlotToken> findById(String id, String tenantId) {
        EvtolFlightTrajectorySlotToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
