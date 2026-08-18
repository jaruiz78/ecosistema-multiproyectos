package com.corp.proyectoeucbamcarboncompliance.infrastructure.adapter.out.persistence;

import com.corp.proyectoeucbamcarboncompliance.domain.model.CbamEmbeddedEmissionsDeclarationToken;
import com.corp.proyectoeucbamcarboncompliance.domain.port.out.CbamEmbeddedEmissionsDeclarationTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCbamEmbeddedEmissionsDeclarationTokenRepositoryAdapter implements CbamEmbeddedEmissionsDeclarationTokenRepositoryPort {

    private final ConcurrentMap<String, CbamEmbeddedEmissionsDeclarationToken> storage = new ConcurrentHashMap<>();

    @Override
    public CbamEmbeddedEmissionsDeclarationToken save(CbamEmbeddedEmissionsDeclarationToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CbamEmbeddedEmissionsDeclarationToken> findById(String id, String tenantId) {
        CbamEmbeddedEmissionsDeclarationToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
