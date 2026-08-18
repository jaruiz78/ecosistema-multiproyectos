package com.corp.proyectodistrictheatingcoolingtwin.infrastructure.adapter.out.persistence;

import com.corp.proyectodistrictheatingcoolingtwin.domain.model.DistrictThermalSubstationNode;
import com.corp.proyectodistrictheatingcoolingtwin.domain.port.out.DistrictThermalSubstationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDistrictThermalSubstationNodeRepositoryAdapter implements DistrictThermalSubstationNodeRepositoryPort {

    private final ConcurrentMap<String, DistrictThermalSubstationNode> storage = new ConcurrentHashMap<>();

    @Override
    public DistrictThermalSubstationNode save(DistrictThermalSubstationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DistrictThermalSubstationNode> findById(String id, String tenantId) {
        DistrictThermalSubstationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
