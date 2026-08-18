package com.corp.proyectodesertdustairqualitygrid.infrastructure.adapter.out.persistence;

import com.corp.proyectodesertdustairqualitygrid.domain.model.MineralDustAerosolOpticalDepthNode;
import com.corp.proyectodesertdustairqualitygrid.domain.port.out.MineralDustAerosolOpticalDepthNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMineralDustAerosolOpticalDepthNodeRepositoryAdapter implements MineralDustAerosolOpticalDepthNodeRepositoryPort {

    private final ConcurrentMap<String, MineralDustAerosolOpticalDepthNode> storage = new ConcurrentHashMap<>();

    @Override
    public MineralDustAerosolOpticalDepthNode save(MineralDustAerosolOpticalDepthNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MineralDustAerosolOpticalDepthNode> findById(String id, String tenantId) {
        MineralDustAerosolOpticalDepthNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
