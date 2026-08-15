package com.corp.proyectobiotecnologia.infrastructure.adapter.out.persistence;

import com.corp.proyectobiotecnologia.domain.model.BioCompound;
import com.corp.proyectobiotecnologia.domain.port.out.BioCompoundRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryBioCompoundRepositoryAdapter implements BioCompoundRepositoryPort {

    private final ConcurrentMap<String, BioCompound> storage = new ConcurrentHashMap<>();

    @Override
    public BioCompound save(BioCompound entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BioCompound> findById(String id, String tenantId) {
        BioCompound entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
