package com.corp.proyectosinglecellspatialomics.infrastructure.adapter.out.persistence;

import com.corp.proyectosinglecellspatialomics.domain.model.SpatialTranscriptomeCellSpotNode;
import com.corp.proyectosinglecellspatialomics.domain.port.out.SpatialTranscriptomeCellSpotNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySpatialTranscriptomeCellSpotNodeRepositoryAdapter implements SpatialTranscriptomeCellSpotNodeRepositoryPort {

    private final ConcurrentMap<String, SpatialTranscriptomeCellSpotNode> storage = new ConcurrentHashMap<>();

    @Override
    public SpatialTranscriptomeCellSpotNode save(SpatialTranscriptomeCellSpotNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpatialTranscriptomeCellSpotNode> findById(String id, String tenantId) {
        SpatialTranscriptomeCellSpotNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
