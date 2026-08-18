package com.corp.proyectocircularbiomassbiorefinery.infrastructure.adapter.out.persistence;

import com.corp.proyectocircularbiomassbiorefinery.domain.model.BiomassFeedstockBatch;
import com.corp.proyectocircularbiomassbiorefinery.domain.port.out.BiomassFeedstockBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBiomassFeedstockBatchRepositoryAdapter implements BiomassFeedstockBatchRepositoryPort {

    private final ConcurrentMap<String, BiomassFeedstockBatch> storage = new ConcurrentHashMap<>();

    @Override
    public BiomassFeedstockBatch save(BiomassFeedstockBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BiomassFeedstockBatch> findById(String id, String tenantId) {
        BiomassFeedstockBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
