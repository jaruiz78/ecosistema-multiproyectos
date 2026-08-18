package com.corp.proyectointerplanetarydelaytolerantrelay.infrastructure.adapter.out.persistence;

import com.corp.proyectointerplanetarydelaytolerantrelay.domain.model.DtnBundleCustodyTransferToken;
import com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.out.DtnBundleCustodyTransferTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDtnBundleCustodyTransferTokenRepositoryAdapter implements DtnBundleCustodyTransferTokenRepositoryPort {

    private final ConcurrentMap<String, DtnBundleCustodyTransferToken> storage = new ConcurrentHashMap<>();

    @Override
    public DtnBundleCustodyTransferToken save(DtnBundleCustodyTransferToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DtnBundleCustodyTransferToken> findById(String id, String tenantId) {
        DtnBundleCustodyTransferToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
