package com.corp.proyectosalud.infrastructure.adapter.out.persistence;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import com.corp.proyectosalud.domain.port.out.ClinicalTrialSampleRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryClinicalTrialSampleRepositoryAdapter implements ClinicalTrialSampleRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialSample> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialSample save(ClinicalTrialSample entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialSample> findById(String id, String tenantId) {
        ClinicalTrialSample entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
