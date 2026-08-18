package com.corp.proyectohapticculturaltelepresence.infrastructure.adapter.out.persistence;

import com.corp.proyectohapticculturaltelepresence.domain.model.HapticExperienceStreamToken;
import com.corp.proyectohapticculturaltelepresence.domain.port.out.HapticExperienceStreamTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHapticExperienceStreamTokenRepositoryAdapter implements HapticExperienceStreamTokenRepositoryPort {

    private final ConcurrentMap<String, HapticExperienceStreamToken> storage = new ConcurrentHashMap<>();

    @Override
    public HapticExperienceStreamToken save(HapticExperienceStreamToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HapticExperienceStreamToken> findById(String id, String tenantId) {
        HapticExperienceStreamToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
