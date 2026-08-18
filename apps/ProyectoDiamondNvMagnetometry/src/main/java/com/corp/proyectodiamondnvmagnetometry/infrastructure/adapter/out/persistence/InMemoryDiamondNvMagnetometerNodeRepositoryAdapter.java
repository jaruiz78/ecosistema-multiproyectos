package com.corp.proyectodiamondnvmagnetometry.infrastructure.adapter.out.persistence;

import com.corp.proyectodiamondnvmagnetometry.domain.model.DiamondNvMagnetometerNode;
import com.corp.proyectodiamondnvmagnetometry.domain.port.out.DiamondNvMagnetometerNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDiamondNvMagnetometerNodeRepositoryAdapter implements DiamondNvMagnetometerNodeRepositoryPort {

    private final ConcurrentMap<String, DiamondNvMagnetometerNode> storage = new ConcurrentHashMap<>();

    @Override
    public DiamondNvMagnetometerNode save(DiamondNvMagnetometerNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DiamondNvMagnetometerNode> findById(String id, String tenantId) {
        DiamondNvMagnetometerNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
