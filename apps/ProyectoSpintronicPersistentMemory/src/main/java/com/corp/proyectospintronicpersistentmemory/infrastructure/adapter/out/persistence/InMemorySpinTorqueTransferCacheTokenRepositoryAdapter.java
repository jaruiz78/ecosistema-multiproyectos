package com.corp.proyectospintronicpersistentmemory.infrastructure.adapter.out.persistence;

import com.corp.proyectospintronicpersistentmemory.domain.model.SpinTorqueTransferCacheToken;
import com.corp.proyectospintronicpersistentmemory.domain.port.out.SpinTorqueTransferCacheTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySpinTorqueTransferCacheTokenRepositoryAdapter implements SpinTorqueTransferCacheTokenRepositoryPort {

    private final ConcurrentMap<String, SpinTorqueTransferCacheToken> storage = new ConcurrentHashMap<>();

    @Override
    public SpinTorqueTransferCacheToken save(SpinTorqueTransferCacheToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpinTorqueTransferCacheToken> findById(String id, String tenantId) {
        SpinTorqueTransferCacheToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
