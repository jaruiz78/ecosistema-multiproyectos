package com.corp.proyectotokenizedcarbonsatellitemrv.infrastructure.adapter.out.persistence;

import com.corp.proyectotokenizedcarbonsatellitemrv.domain.model.VerifiedCarbonSequestrationCreditToken;
import com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.out.VerifiedCarbonSequestrationCreditTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryVerifiedCarbonSequestrationCreditTokenRepositoryAdapter implements VerifiedCarbonSequestrationCreditTokenRepositoryPort {

    private final ConcurrentMap<String, VerifiedCarbonSequestrationCreditToken> storage = new ConcurrentHashMap<>();

    @Override
    public VerifiedCarbonSequestrationCreditToken save(VerifiedCarbonSequestrationCreditToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VerifiedCarbonSequestrationCreditToken> findById(String id, String tenantId) {
        VerifiedCarbonSequestrationCreditToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
