package com.corp.proyectocyanobacteriabionitrogenfertilizer.infrastructure.adapter.out.persistence;

import com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.model.HeterocystNitrogenaseFluxNode;
import com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.port.out.HeterocystNitrogenaseFluxNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHeterocystNitrogenaseFluxNodeRepositoryAdapter implements HeterocystNitrogenaseFluxNodeRepositoryPort {

    private final ConcurrentMap<String, HeterocystNitrogenaseFluxNode> storage = new ConcurrentHashMap<>();

    @Override
    public HeterocystNitrogenaseFluxNode save(HeterocystNitrogenaseFluxNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HeterocystNitrogenaseFluxNode> findById(String id, String tenantId) {
        HeterocystNitrogenaseFluxNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
