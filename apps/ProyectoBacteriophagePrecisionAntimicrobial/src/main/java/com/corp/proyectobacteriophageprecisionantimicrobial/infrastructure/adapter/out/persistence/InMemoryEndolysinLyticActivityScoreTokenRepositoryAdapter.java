package com.corp.proyectobacteriophageprecisionantimicrobial.infrastructure.adapter.out.persistence;

import com.corp.proyectobacteriophageprecisionantimicrobial.domain.model.EndolysinLyticActivityScoreToken;
import com.corp.proyectobacteriophageprecisionantimicrobial.domain.port.out.EndolysinLyticActivityScoreTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEndolysinLyticActivityScoreTokenRepositoryAdapter implements EndolysinLyticActivityScoreTokenRepositoryPort {

    private final ConcurrentMap<String, EndolysinLyticActivityScoreToken> storage = new ConcurrentHashMap<>();

    @Override
    public EndolysinLyticActivityScoreToken save(EndolysinLyticActivityScoreToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EndolysinLyticActivityScoreToken> findById(String id, String tenantId) {
        EndolysinLyticActivityScoreToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
