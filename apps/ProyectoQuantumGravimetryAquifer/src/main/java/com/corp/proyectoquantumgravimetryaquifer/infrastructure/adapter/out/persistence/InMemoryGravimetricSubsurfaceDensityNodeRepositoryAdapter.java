package com.corp.proyectoquantumgravimetryaquifer.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumgravimetryaquifer.domain.model.GravimetricSubsurfaceDensityNode;
import com.corp.proyectoquantumgravimetryaquifer.domain.port.out.GravimetricSubsurfaceDensityNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryGravimetricSubsurfaceDensityNodeRepositoryAdapter implements GravimetricSubsurfaceDensityNodeRepositoryPort {

    private final ConcurrentMap<String, GravimetricSubsurfaceDensityNode> storage = new ConcurrentHashMap<>();

    @Override
    public GravimetricSubsurfaceDensityNode save(GravimetricSubsurfaceDensityNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GravimetricSubsurfaceDensityNode> findById(String id, String tenantId) {
        GravimetricSubsurfaceDensityNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
