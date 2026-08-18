package com.corp.proyectoneuromorphicedgesnn.infrastructure.adapter.out.persistence;

import com.corp.proyectoneuromorphicedgesnn.domain.model.NeuromorphicSpikeEventNode;
import com.corp.proyectoneuromorphicedgesnn.domain.port.out.NeuromorphicSpikeEventNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryNeuromorphicSpikeEventNodeRepositoryAdapter implements NeuromorphicSpikeEventNodeRepositoryPort {

    private final ConcurrentMap<String, NeuromorphicSpikeEventNode> storage = new ConcurrentHashMap<>();

    @Override
    public NeuromorphicSpikeEventNode save(NeuromorphicSpikeEventNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<NeuromorphicSpikeEventNode> findById(String id, String tenantId) {
        NeuromorphicSpikeEventNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
