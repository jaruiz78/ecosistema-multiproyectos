package com.corp.proyectohealthfederatedclinical.infrastructure.adapter.out.persistence;

import com.corp.proyectohealthfederatedclinical.domain.model.ClinicalTrialEnclave;
import com.corp.proyectohealthfederatedclinical.domain.port.out.ClinicalTrialEnclaveRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryClinicalTrialEnclaveRepositoryAdapter implements ClinicalTrialEnclaveRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialEnclave> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialEnclave save(ClinicalTrialEnclave entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialEnclave> findById(String id, String tenantId) {
        ClinicalTrialEnclave entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
