package com.corp.proyectoparametricweatherinsurance.infrastructure.adapter.out.persistence;

import com.corp.proyectoparametricweatherinsurance.domain.model.ParametricInsurancePolicyPayoutToken;
import com.corp.proyectoparametricweatherinsurance.domain.port.out.ParametricInsurancePolicyPayoutTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryParametricInsurancePolicyPayoutTokenRepositoryAdapter implements ParametricInsurancePolicyPayoutTokenRepositoryPort {

    private final ConcurrentMap<String, ParametricInsurancePolicyPayoutToken> storage = new ConcurrentHashMap<>();

    @Override
    public ParametricInsurancePolicyPayoutToken save(ParametricInsurancePolicyPayoutToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ParametricInsurancePolicyPayoutToken> findById(String id, String tenantId) {
        ParametricInsurancePolicyPayoutToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
