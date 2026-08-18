package com.corp.proyectomicroalgaecarbonbiofuel.infrastructure.adapter.out.persistence;

import com.corp.proyectomicroalgaecarbonbiofuel.domain.model.AlgaeBioreactorCultureBatch;
import com.corp.proyectomicroalgaecarbonbiofuel.domain.port.out.AlgaeBioreactorCultureBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAlgaeBioreactorCultureBatchRepositoryAdapter implements AlgaeBioreactorCultureBatchRepositoryPort {

    private final ConcurrentMap<String, AlgaeBioreactorCultureBatch> storage = new ConcurrentHashMap<>();

    @Override
    public AlgaeBioreactorCultureBatch save(AlgaeBioreactorCultureBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AlgaeBioreactorCultureBatch> findById(String id, String tenantId) {
        AlgaeBioreactorCultureBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
