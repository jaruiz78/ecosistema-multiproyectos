package com.corp.proyectooceanplasticcleanuprouter.infrastructure.adapter.out.persistence;

import com.corp.proyectooceanplasticcleanuprouter.domain.model.MicroplasticDensityConcentrationNode;
import com.corp.proyectooceanplasticcleanuprouter.domain.port.out.MicroplasticDensityConcentrationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMicroplasticDensityConcentrationNodeRepositoryAdapter implements MicroplasticDensityConcentrationNodeRepositoryPort {

    private final ConcurrentMap<String, MicroplasticDensityConcentrationNode> storage = new ConcurrentHashMap<>();

    @Override
    public MicroplasticDensityConcentrationNode save(MicroplasticDensityConcentrationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MicroplasticDensityConcentrationNode> findById(String id, String tenantId) {
        MicroplasticDensityConcentrationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
