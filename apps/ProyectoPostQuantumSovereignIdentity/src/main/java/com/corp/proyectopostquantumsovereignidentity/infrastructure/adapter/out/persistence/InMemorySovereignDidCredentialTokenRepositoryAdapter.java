package com.corp.proyectopostquantumsovereignidentity.infrastructure.adapter.out.persistence;

import com.corp.proyectopostquantumsovereignidentity.domain.model.SovereignDidCredentialToken;
import com.corp.proyectopostquantumsovereignidentity.domain.port.out.SovereignDidCredentialTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySovereignDidCredentialTokenRepositoryAdapter implements SovereignDidCredentialTokenRepositoryPort {

    private final ConcurrentMap<String, SovereignDidCredentialToken> storage = new ConcurrentHashMap<>();

    @Override
    public SovereignDidCredentialToken save(SovereignDidCredentialToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SovereignDidCredentialToken> findById(String id, String tenantId) {
        SovereignDidCredentialToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
