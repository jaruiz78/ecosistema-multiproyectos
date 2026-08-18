package com.corp.proyectoopticalsatellitegroundstation.infrastructure.adapter.out.persistence;

import com.corp.proyectoopticalsatellitegroundstation.domain.model.StrehlRatioWavefrontCorrectionNode;
import com.corp.proyectoopticalsatellitegroundstation.domain.port.out.StrehlRatioWavefrontCorrectionNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryStrehlRatioWavefrontCorrectionNodeRepositoryAdapter implements StrehlRatioWavefrontCorrectionNodeRepositoryPort {

    private final ConcurrentMap<String, StrehlRatioWavefrontCorrectionNode> storage = new ConcurrentHashMap<>();

    @Override
    public StrehlRatioWavefrontCorrectionNode save(StrehlRatioWavefrontCorrectionNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<StrehlRatioWavefrontCorrectionNode> findById(String id, String tenantId) {
        StrehlRatioWavefrontCorrectionNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
