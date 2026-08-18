package com.corp.proyectospacedebrislasermitigation.infrastructure.adapter.out.persistence;

import com.corp.proyectospacedebrislasermitigation.domain.model.SpaceDebrisConjunctionTrackToken;
import com.corp.proyectospacedebrislasermitigation.domain.port.out.SpaceDebrisConjunctionTrackTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySpaceDebrisConjunctionTrackTokenRepositoryAdapter implements SpaceDebrisConjunctionTrackTokenRepositoryPort {

    private final ConcurrentMap<String, SpaceDebrisConjunctionTrackToken> storage = new ConcurrentHashMap<>();

    @Override
    public SpaceDebrisConjunctionTrackToken save(SpaceDebrisConjunctionTrackToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpaceDebrisConjunctionTrackToken> findById(String id, String tenantId) {
        SpaceDebrisConjunctionTrackToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
