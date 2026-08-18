package com.corp.proyectostemcellorgantissuebioprinting.infrastructure.adapter.out.persistence;

import com.corp.proyectostemcellorgantissuebioprinting.domain.model.BioinkScaffoldPerfusionGridNode;
import com.corp.proyectostemcellorgantissuebioprinting.domain.port.out.BioinkScaffoldPerfusionGridNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBioinkScaffoldPerfusionGridNodeRepositoryAdapter implements BioinkScaffoldPerfusionGridNodeRepositoryPort {

    private final ConcurrentMap<String, BioinkScaffoldPerfusionGridNode> storage = new ConcurrentHashMap<>();

    @Override
    public BioinkScaffoldPerfusionGridNode save(BioinkScaffoldPerfusionGridNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BioinkScaffoldPerfusionGridNode> findById(String id, String tenantId) {
        BioinkScaffoldPerfusionGridNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
