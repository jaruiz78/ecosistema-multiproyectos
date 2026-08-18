package com.corp.proyectospintronicterahertzemitter.infrastructure.adapter.out.persistence;

import com.corp.proyectospintronicterahertzemitter.domain.model.SpintronicThzPulseWaveformNode;
import com.corp.proyectospintronicterahertzemitter.domain.port.out.SpintronicThzPulseWaveformNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySpintronicThzPulseWaveformNodeRepositoryAdapter implements SpintronicThzPulseWaveformNodeRepositoryPort {

    private final ConcurrentMap<String, SpintronicThzPulseWaveformNode> storage = new ConcurrentHashMap<>();

    @Override
    public SpintronicThzPulseWaveformNode save(SpintronicThzPulseWaveformNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpintronicThzPulseWaveformNode> findById(String id, String tenantId) {
        SpintronicThzPulseWaveformNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
