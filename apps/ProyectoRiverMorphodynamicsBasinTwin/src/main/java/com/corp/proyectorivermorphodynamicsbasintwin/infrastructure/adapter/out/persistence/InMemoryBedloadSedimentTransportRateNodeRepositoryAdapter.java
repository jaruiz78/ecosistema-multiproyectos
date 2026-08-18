package com.corp.proyectorivermorphodynamicsbasintwin.infrastructure.adapter.out.persistence;

import com.corp.proyectorivermorphodynamicsbasintwin.domain.model.BedloadSedimentTransportRateNode;
import com.corp.proyectorivermorphodynamicsbasintwin.domain.port.out.BedloadSedimentTransportRateNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBedloadSedimentTransportRateNodeRepositoryAdapter implements BedloadSedimentTransportRateNodeRepositoryPort {

    private final ConcurrentMap<String, BedloadSedimentTransportRateNode> storage = new ConcurrentHashMap<>();

    @Override
    public BedloadSedimentTransportRateNode save(BedloadSedimentTransportRateNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BedloadSedimentTransportRateNode> findById(String id, String tenantId) {
        BedloadSedimentTransportRateNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
