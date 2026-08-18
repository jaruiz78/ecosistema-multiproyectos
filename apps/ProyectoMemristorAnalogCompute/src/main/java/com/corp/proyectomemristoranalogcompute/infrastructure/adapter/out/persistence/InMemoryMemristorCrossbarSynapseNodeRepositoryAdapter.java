package com.corp.proyectomemristoranalogcompute.infrastructure.adapter.out.persistence;

import com.corp.proyectomemristoranalogcompute.domain.model.MemristorCrossbarSynapseNode;
import com.corp.proyectomemristoranalogcompute.domain.port.out.MemristorCrossbarSynapseNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMemristorCrossbarSynapseNodeRepositoryAdapter implements MemristorCrossbarSynapseNodeRepositoryPort {

    private final ConcurrentMap<String, MemristorCrossbarSynapseNode> storage = new ConcurrentHashMap<>();

    @Override
    public MemristorCrossbarSynapseNode save(MemristorCrossbarSynapseNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MemristorCrossbarSynapseNode> findById(String id, String tenantId) {
        MemristorCrossbarSynapseNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
