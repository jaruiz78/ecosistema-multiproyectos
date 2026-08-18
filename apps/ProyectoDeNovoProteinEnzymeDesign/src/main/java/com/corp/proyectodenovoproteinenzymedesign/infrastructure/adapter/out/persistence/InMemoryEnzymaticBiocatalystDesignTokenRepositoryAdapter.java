package com.corp.proyectodenovoproteinenzymedesign.infrastructure.adapter.out.persistence;

import com.corp.proyectodenovoproteinenzymedesign.domain.model.EnzymaticBiocatalystDesignToken;
import com.corp.proyectodenovoproteinenzymedesign.domain.port.out.EnzymaticBiocatalystDesignTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEnzymaticBiocatalystDesignTokenRepositoryAdapter implements EnzymaticBiocatalystDesignTokenRepositoryPort {

    private final ConcurrentMap<String, EnzymaticBiocatalystDesignToken> storage = new ConcurrentHashMap<>();

    @Override
    public EnzymaticBiocatalystDesignToken save(EnzymaticBiocatalystDesignToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EnzymaticBiocatalystDesignToken> findById(String id, String tenantId) {
        EnzymaticBiocatalystDesignToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
