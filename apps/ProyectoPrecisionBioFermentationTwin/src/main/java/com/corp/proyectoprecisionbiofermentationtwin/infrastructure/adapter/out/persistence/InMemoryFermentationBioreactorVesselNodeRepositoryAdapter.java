package com.corp.proyectoprecisionbiofermentationtwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoprecisionbiofermentationtwin.domain.model.FermentationBioreactorVesselNode;
import com.corp.proyectoprecisionbiofermentationtwin.domain.port.out.FermentationBioreactorVesselNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryFermentationBioreactorVesselNodeRepositoryAdapter implements FermentationBioreactorVesselNodeRepositoryPort {

    private final ConcurrentMap<String, FermentationBioreactorVesselNode> storage = new ConcurrentHashMap<>();

    @Override
    public FermentationBioreactorVesselNode save(FermentationBioreactorVesselNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<FermentationBioreactorVesselNode> findById(String id, String tenantId) {
        FermentationBioreactorVesselNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
