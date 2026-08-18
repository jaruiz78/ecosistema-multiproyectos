package com.corp.proyectoartificialchloroplastcarbonsink.infrastructure.adapter.out.persistence;

import com.corp.proyectoartificialchloroplastcarbonsink.domain.model.SyntheticThylakoidEfficiencyToken;
import com.corp.proyectoartificialchloroplastcarbonsink.domain.port.out.SyntheticThylakoidEfficiencyTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySyntheticThylakoidEfficiencyTokenRepositoryAdapter implements SyntheticThylakoidEfficiencyTokenRepositoryPort {

    private final ConcurrentMap<String, SyntheticThylakoidEfficiencyToken> storage = new ConcurrentHashMap<>();

    @Override
    public SyntheticThylakoidEfficiencyToken save(SyntheticThylakoidEfficiencyToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SyntheticThylakoidEfficiencyToken> findById(String id, String tenantId) {
        SyntheticThylakoidEfficiencyToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
