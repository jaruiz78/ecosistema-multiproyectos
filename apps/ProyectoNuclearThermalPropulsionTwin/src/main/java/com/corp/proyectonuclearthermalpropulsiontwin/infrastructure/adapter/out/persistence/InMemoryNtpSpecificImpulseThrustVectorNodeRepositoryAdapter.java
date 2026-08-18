package com.corp.proyectonuclearthermalpropulsiontwin.infrastructure.adapter.out.persistence;

import com.corp.proyectonuclearthermalpropulsiontwin.domain.model.NtpSpecificImpulseThrustVectorNode;
import com.corp.proyectonuclearthermalpropulsiontwin.domain.port.out.NtpSpecificImpulseThrustVectorNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryNtpSpecificImpulseThrustVectorNodeRepositoryAdapter implements NtpSpecificImpulseThrustVectorNodeRepositoryPort {

    private final ConcurrentMap<String, NtpSpecificImpulseThrustVectorNode> storage = new ConcurrentHashMap<>();

    @Override
    public NtpSpecificImpulseThrustVectorNode save(NtpSpecificImpulseThrustVectorNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<NtpSpecificImpulseThrustVectorNode> findById(String id, String tenantId) {
        NtpSpecificImpulseThrustVectorNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
