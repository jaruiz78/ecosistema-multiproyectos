package com.corp.proyectocircadianlightingsmartcity.infrastructure.adapter.out.persistence;

import com.corp.proyectocircadianlightingsmartcity.domain.model.CircadianLightingMelanopicNode;
import com.corp.proyectocircadianlightingsmartcity.domain.port.out.CircadianLightingMelanopicNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCircadianLightingMelanopicNodeRepositoryAdapter implements CircadianLightingMelanopicNodeRepositoryPort {

    private final ConcurrentMap<String, CircadianLightingMelanopicNode> storage = new ConcurrentHashMap<>();

    @Override
    public CircadianLightingMelanopicNode save(CircadianLightingMelanopicNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CircadianLightingMelanopicNode> findById(String id, String tenantId) {
        CircadianLightingMelanopicNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
