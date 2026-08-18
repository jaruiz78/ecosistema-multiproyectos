package com.corp.proyectoquantumtimedistributionnetwork.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumtimedistributionnetwork.domain.model.QuantumPicosecondClockSyncToken;
import com.corp.proyectoquantumtimedistributionnetwork.domain.port.out.QuantumPicosecondClockSyncTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryQuantumPicosecondClockSyncTokenRepositoryAdapter implements QuantumPicosecondClockSyncTokenRepositoryPort {

    private final ConcurrentMap<String, QuantumPicosecondClockSyncToken> storage = new ConcurrentHashMap<>();

    @Override
    public QuantumPicosecondClockSyncToken save(QuantumPicosecondClockSyncToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QuantumPicosecondClockSyncToken> findById(String id, String tenantId) {
        QuantumPicosecondClockSyncToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
