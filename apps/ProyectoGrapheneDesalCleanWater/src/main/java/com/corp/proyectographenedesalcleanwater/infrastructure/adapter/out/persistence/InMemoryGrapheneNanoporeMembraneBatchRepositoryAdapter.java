package com.corp.proyectographenedesalcleanwater.infrastructure.adapter.out.persistence;

import com.corp.proyectographenedesalcleanwater.domain.model.GrapheneNanoporeMembraneBatch;
import com.corp.proyectographenedesalcleanwater.domain.port.out.GrapheneNanoporeMembraneBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryGrapheneNanoporeMembraneBatchRepositoryAdapter implements GrapheneNanoporeMembraneBatchRepositoryPort {

    private final ConcurrentMap<String, GrapheneNanoporeMembraneBatch> storage = new ConcurrentHashMap<>();

    @Override
    public GrapheneNanoporeMembraneBatch save(GrapheneNanoporeMembraneBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GrapheneNanoporeMembraneBatch> findById(String id, String tenantId) {
        GrapheneNanoporeMembraneBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
