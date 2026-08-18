package com.corp.proyectoaavvectortherapeuticdesign.infrastructure.adapter.out.persistence;

import com.corp.proyectoaavvectortherapeuticdesign.domain.model.AavCapsidTropismVectorToken;
import com.corp.proyectoaavvectortherapeuticdesign.domain.port.out.AavCapsidTropismVectorTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAavCapsidTropismVectorTokenRepositoryAdapter implements AavCapsidTropismVectorTokenRepositoryPort {

    private final ConcurrentMap<String, AavCapsidTropismVectorToken> storage = new ConcurrentHashMap<>();

    @Override
    public AavCapsidTropismVectorToken save(AavCapsidTropismVectorToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AavCapsidTropismVectorToken> findById(String id, String tenantId) {
        AavCapsidTropismVectorToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
