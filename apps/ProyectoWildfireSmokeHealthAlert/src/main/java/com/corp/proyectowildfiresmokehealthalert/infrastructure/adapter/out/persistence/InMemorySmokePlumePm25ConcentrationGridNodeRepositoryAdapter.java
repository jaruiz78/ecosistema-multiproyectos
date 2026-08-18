package com.corp.proyectowildfiresmokehealthalert.infrastructure.adapter.out.persistence;

import com.corp.proyectowildfiresmokehealthalert.domain.model.SmokePlumePm25ConcentrationGridNode;
import com.corp.proyectowildfiresmokehealthalert.domain.port.out.SmokePlumePm25ConcentrationGridNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySmokePlumePm25ConcentrationGridNodeRepositoryAdapter implements SmokePlumePm25ConcentrationGridNodeRepositoryPort {

    private final ConcurrentMap<String, SmokePlumePm25ConcentrationGridNode> storage = new ConcurrentHashMap<>();

    @Override
    public SmokePlumePm25ConcentrationGridNode save(SmokePlumePm25ConcentrationGridNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SmokePlumePm25ConcentrationGridNode> findById(String id, String tenantId) {
        SmokePlumePm25ConcentrationGridNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
