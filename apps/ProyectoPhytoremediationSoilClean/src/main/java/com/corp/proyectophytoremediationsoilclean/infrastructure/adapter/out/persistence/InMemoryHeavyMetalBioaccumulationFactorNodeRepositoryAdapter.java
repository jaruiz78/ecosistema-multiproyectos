package com.corp.proyectophytoremediationsoilclean.infrastructure.adapter.out.persistence;

import com.corp.proyectophytoremediationsoilclean.domain.model.HeavyMetalBioaccumulationFactorNode;
import com.corp.proyectophytoremediationsoilclean.domain.port.out.HeavyMetalBioaccumulationFactorNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHeavyMetalBioaccumulationFactorNodeRepositoryAdapter implements HeavyMetalBioaccumulationFactorNodeRepositoryPort {

    private final ConcurrentMap<String, HeavyMetalBioaccumulationFactorNode> storage = new ConcurrentHashMap<>();

    @Override
    public HeavyMetalBioaccumulationFactorNode save(HeavyMetalBioaccumulationFactorNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HeavyMetalBioaccumulationFactorNode> findById(String id, String tenantId) {
        HeavyMetalBioaccumulationFactorNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
