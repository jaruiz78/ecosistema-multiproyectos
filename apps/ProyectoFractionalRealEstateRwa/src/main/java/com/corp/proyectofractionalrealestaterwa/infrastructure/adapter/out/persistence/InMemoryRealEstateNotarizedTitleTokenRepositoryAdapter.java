package com.corp.proyectofractionalrealestaterwa.infrastructure.adapter.out.persistence;

import com.corp.proyectofractionalrealestaterwa.domain.model.RealEstateNotarizedTitleToken;
import com.corp.proyectofractionalrealestaterwa.domain.port.out.RealEstateNotarizedTitleTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryRealEstateNotarizedTitleTokenRepositoryAdapter implements RealEstateNotarizedTitleTokenRepositoryPort {

    private final ConcurrentMap<String, RealEstateNotarizedTitleToken> storage = new ConcurrentHashMap<>();

    @Override
    public RealEstateNotarizedTitleToken save(RealEstateNotarizedTitleToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<RealEstateNotarizedTitleToken> findById(String id, String tenantId) {
        RealEstateNotarizedTitleToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
