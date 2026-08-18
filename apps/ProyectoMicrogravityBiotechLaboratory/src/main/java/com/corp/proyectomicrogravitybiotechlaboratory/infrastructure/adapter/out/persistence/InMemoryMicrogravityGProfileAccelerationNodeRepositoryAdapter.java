package com.corp.proyectomicrogravitybiotechlaboratory.infrastructure.adapter.out.persistence;

import com.corp.proyectomicrogravitybiotechlaboratory.domain.model.MicrogravityGProfileAccelerationNode;
import com.corp.proyectomicrogravitybiotechlaboratory.domain.port.out.MicrogravityGProfileAccelerationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryMicrogravityGProfileAccelerationNodeRepositoryAdapter implements MicrogravityGProfileAccelerationNodeRepositoryPort {

    private final ConcurrentMap<String, MicrogravityGProfileAccelerationNode> storage = new ConcurrentHashMap<>();

    @Override
    public MicrogravityGProfileAccelerationNode save(MicrogravityGProfileAccelerationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MicrogravityGProfileAccelerationNode> findById(String id, String tenantId) {
        MicrogravityGProfileAccelerationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
