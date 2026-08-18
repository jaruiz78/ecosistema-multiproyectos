package com.corp.proyectophagedisplaydirectedevolution.infrastructure.adapter.out.persistence;

import com.corp.proyectophagedisplaydirectedevolution.domain.model.PhageDisplayAffinityEnrichmentToken;
import com.corp.proyectophagedisplaydirectedevolution.domain.port.out.PhageDisplayAffinityEnrichmentTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPhageDisplayAffinityEnrichmentTokenRepositoryAdapter implements PhageDisplayAffinityEnrichmentTokenRepositoryPort {

    private final ConcurrentMap<String, PhageDisplayAffinityEnrichmentToken> storage = new ConcurrentHashMap<>();

    @Override
    public PhageDisplayAffinityEnrichmentToken save(PhageDisplayAffinityEnrichmentToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PhageDisplayAffinityEnrichmentToken> findById(String id, String tenantId) {
        PhageDisplayAffinityEnrichmentToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
