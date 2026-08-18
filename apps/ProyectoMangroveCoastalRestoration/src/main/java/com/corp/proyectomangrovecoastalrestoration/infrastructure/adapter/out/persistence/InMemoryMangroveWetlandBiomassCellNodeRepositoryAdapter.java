package com.corp.proyectomangrovecoastalrestoration.infrastructure.adapter.out.persistence;

import com.corp.proyectomangrovecoastalrestoration.domain.model.MangroveWetlandBiomassCellNode;
import com.corp.proyectomangrovecoastalrestoration.domain.port.out.MangroveWetlandBiomassCellNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMangroveWetlandBiomassCellNodeRepositoryAdapter implements MangroveWetlandBiomassCellNodeRepositoryPort {

    private final ConcurrentMap<String, MangroveWetlandBiomassCellNode> storage = new ConcurrentHashMap<>();

    @Override
    public MangroveWetlandBiomassCellNode save(MangroveWetlandBiomassCellNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MangroveWetlandBiomassCellNode> findById(String id, String tenantId) {
        MangroveWetlandBiomassCellNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
