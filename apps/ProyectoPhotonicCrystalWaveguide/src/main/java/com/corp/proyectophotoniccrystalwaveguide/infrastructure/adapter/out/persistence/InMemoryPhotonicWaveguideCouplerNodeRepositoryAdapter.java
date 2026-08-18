package com.corp.proyectophotoniccrystalwaveguide.infrastructure.adapter.out.persistence;

import com.corp.proyectophotoniccrystalwaveguide.domain.model.PhotonicWaveguideCouplerNode;
import com.corp.proyectophotoniccrystalwaveguide.domain.port.out.PhotonicWaveguideCouplerNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPhotonicWaveguideCouplerNodeRepositoryAdapter implements PhotonicWaveguideCouplerNodeRepositoryPort {

    private final ConcurrentMap<String, PhotonicWaveguideCouplerNode> storage = new ConcurrentHashMap<>();

    @Override
    public PhotonicWaveguideCouplerNode save(PhotonicWaveguideCouplerNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PhotonicWaveguideCouplerNode> findById(String id, String tenantId) {
        PhotonicWaveguideCouplerNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
