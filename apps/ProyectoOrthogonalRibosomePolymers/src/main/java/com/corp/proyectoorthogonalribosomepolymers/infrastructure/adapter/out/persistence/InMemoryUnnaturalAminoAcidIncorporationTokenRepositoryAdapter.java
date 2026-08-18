package com.corp.proyectoorthogonalribosomepolymers.infrastructure.adapter.out.persistence;

import com.corp.proyectoorthogonalribosomepolymers.domain.model.UnnaturalAminoAcidIncorporationToken;
import com.corp.proyectoorthogonalribosomepolymers.domain.port.out.UnnaturalAminoAcidIncorporationTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUnnaturalAminoAcidIncorporationTokenRepositoryAdapter implements UnnaturalAminoAcidIncorporationTokenRepositoryPort {

    private final ConcurrentMap<String, UnnaturalAminoAcidIncorporationToken> storage = new ConcurrentHashMap<>();

    @Override
    public UnnaturalAminoAcidIncorporationToken save(UnnaturalAminoAcidIncorporationToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<UnnaturalAminoAcidIncorporationToken> findById(String id, String tenantId) {
        UnnaturalAminoAcidIncorporationToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
