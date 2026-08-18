package com.corp.proyectoasteroidresourceprospector.infrastructure.adapter.out.persistence;

import com.corp.proyectoasteroidresourceprospector.domain.model.AsteroidMineralAbundanceScoreToken;
import com.corp.proyectoasteroidresourceprospector.domain.port.out.AsteroidMineralAbundanceScoreTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAsteroidMineralAbundanceScoreTokenRepositoryAdapter implements AsteroidMineralAbundanceScoreTokenRepositoryPort {

    private final ConcurrentMap<String, AsteroidMineralAbundanceScoreToken> storage = new ConcurrentHashMap<>();

    @Override
    public AsteroidMineralAbundanceScoreToken save(AsteroidMineralAbundanceScoreToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AsteroidMineralAbundanceScoreToken> findById(String id, String tenantId) {
        AsteroidMineralAbundanceScoreToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
