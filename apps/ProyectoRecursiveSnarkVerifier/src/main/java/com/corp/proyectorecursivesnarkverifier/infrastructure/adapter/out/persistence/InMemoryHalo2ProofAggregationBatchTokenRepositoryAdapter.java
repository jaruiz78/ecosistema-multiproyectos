package com.corp.proyectorecursivesnarkverifier.infrastructure.adapter.out.persistence;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import com.corp.proyectorecursivesnarkverifier.domain.port.out.Halo2ProofAggregationBatchTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHalo2ProofAggregationBatchTokenRepositoryAdapter implements Halo2ProofAggregationBatchTokenRepositoryPort {

    private final ConcurrentMap<String, Halo2ProofAggregationBatchToken> storage = new ConcurrentHashMap<>();

    @Override
    public Halo2ProofAggregationBatchToken save(Halo2ProofAggregationBatchToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Halo2ProofAggregationBatchToken> findById(String id, String tenantId) {
        Halo2ProofAggregationBatchToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
