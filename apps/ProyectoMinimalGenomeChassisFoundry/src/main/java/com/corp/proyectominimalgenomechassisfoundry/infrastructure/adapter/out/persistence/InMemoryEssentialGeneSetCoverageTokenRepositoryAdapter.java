package com.corp.proyectominimalgenomechassisfoundry.infrastructure.adapter.out.persistence;

import com.corp.proyectominimalgenomechassisfoundry.domain.model.EssentialGeneSetCoverageToken;
import com.corp.proyectominimalgenomechassisfoundry.domain.port.out.EssentialGeneSetCoverageTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEssentialGeneSetCoverageTokenRepositoryAdapter implements EssentialGeneSetCoverageTokenRepositoryPort {

    private final ConcurrentMap<String, EssentialGeneSetCoverageToken> storage = new ConcurrentHashMap<>();

    @Override
    public EssentialGeneSetCoverageToken save(EssentialGeneSetCoverageToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EssentialGeneSetCoverageToken> findById(String id, String tenantId) {
        EssentialGeneSetCoverageToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
