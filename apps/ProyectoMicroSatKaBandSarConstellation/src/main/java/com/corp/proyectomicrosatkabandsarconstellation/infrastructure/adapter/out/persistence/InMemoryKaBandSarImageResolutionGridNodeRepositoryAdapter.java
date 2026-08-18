package com.corp.proyectomicrosatkabandsarconstellation.infrastructure.adapter.out.persistence;

import com.corp.proyectomicrosatkabandsarconstellation.domain.model.KaBandSarImageResolutionGridNode;
import com.corp.proyectomicrosatkabandsarconstellation.domain.port.out.KaBandSarImageResolutionGridNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryKaBandSarImageResolutionGridNodeRepositoryAdapter implements KaBandSarImageResolutionGridNodeRepositoryPort {

    private final ConcurrentMap<String, KaBandSarImageResolutionGridNode> storage = new ConcurrentHashMap<>();

    @Override
    public KaBandSarImageResolutionGridNode save(KaBandSarImageResolutionGridNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<KaBandSarImageResolutionGridNode> findById(String id, String tenantId) {
        KaBandSarImageResolutionGridNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
