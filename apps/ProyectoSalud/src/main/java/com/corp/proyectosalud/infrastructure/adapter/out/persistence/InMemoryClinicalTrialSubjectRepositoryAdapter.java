package com.corp.proyectosalud.infrastructure.adapter.out.persistence;

import com.corp.proyectosalud.domain.model.ClinicalTrialSubject;
import com.corp.proyectosalud.domain.port.out.ClinicalTrialSubjectRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryClinicalTrialSubjectRepositoryAdapter implements ClinicalTrialSubjectRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialSubject> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialSubject save(ClinicalTrialSubject entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialSubject> findById(String id, String tenantId) {
        ClinicalTrialSubject entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
