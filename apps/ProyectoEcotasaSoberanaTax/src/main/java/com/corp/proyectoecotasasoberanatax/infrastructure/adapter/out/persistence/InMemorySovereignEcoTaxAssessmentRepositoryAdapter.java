package com.corp.proyectoecotasasoberanatax.infrastructure.adapter.out.persistence;

import com.corp.proyectoecotasasoberanatax.domain.model.SovereignEcoTaxAssessment;
import com.corp.proyectoecotasasoberanatax.domain.port.out.SovereignEcoTaxAssessmentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySovereignEcoTaxAssessmentRepositoryAdapter implements SovereignEcoTaxAssessmentRepositoryPort {

    private final ConcurrentMap<String, SovereignEcoTaxAssessment> storage = new ConcurrentHashMap<>();

    @Override
    public SovereignEcoTaxAssessment save(SovereignEcoTaxAssessment entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SovereignEcoTaxAssessment> findById(String id, String tenantId) {
        SovereignEcoTaxAssessment entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
