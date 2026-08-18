package com.corp.proyectoeidas2digitalidentitywallet.infrastructure.adapter.out.persistence;

import com.corp.proyectoeidas2digitalidentitywallet.domain.model.VerifiableCredentialStatusListToken;
import com.corp.proyectoeidas2digitalidentitywallet.domain.port.out.VerifiableCredentialStatusListTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryVerifiableCredentialStatusListTokenRepositoryAdapter implements VerifiableCredentialStatusListTokenRepositoryPort {

    private final ConcurrentMap<String, VerifiableCredentialStatusListToken> storage = new ConcurrentHashMap<>();

    @Override
    public VerifiableCredentialStatusListToken save(VerifiableCredentialStatusListToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VerifiableCredentialStatusListToken> findById(String id, String tenantId) {
        VerifiableCredentialStatusListToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
