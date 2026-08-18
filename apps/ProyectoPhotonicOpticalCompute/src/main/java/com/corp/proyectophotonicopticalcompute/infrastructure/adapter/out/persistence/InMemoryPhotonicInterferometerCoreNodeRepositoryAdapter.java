package com.corp.proyectophotonicopticalcompute.infrastructure.adapter.out.persistence;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import com.corp.proyectophotonicopticalcompute.domain.port.out.PhotonicInterferometerCoreNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPhotonicInterferometerCoreNodeRepositoryAdapter implements PhotonicInterferometerCoreNodeRepositoryPort {

    private final ConcurrentMap<String, PhotonicInterferometerCoreNode> storage = new ConcurrentHashMap<>();

    @Override
    public PhotonicInterferometerCoreNode save(PhotonicInterferometerCoreNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PhotonicInterferometerCoreNode> findById(String id, String tenantId) {
        PhotonicInterferometerCoreNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
