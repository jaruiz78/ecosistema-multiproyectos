package com.corp.proyectocubesatconstellationadcs.infrastructure.adapter.out.persistence;

import com.corp.proyectocubesatconstellationadcs.domain.model.CubesatAttitudeQuaternionPointingNode;
import com.corp.proyectocubesatconstellationadcs.domain.port.out.CubesatAttitudeQuaternionPointingNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCubesatAttitudeQuaternionPointingNodeRepositoryAdapter implements CubesatAttitudeQuaternionPointingNodeRepositoryPort {

    private final ConcurrentMap<String, CubesatAttitudeQuaternionPointingNode> storage = new ConcurrentHashMap<>();

    @Override
    public CubesatAttitudeQuaternionPointingNode save(CubesatAttitudeQuaternionPointingNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CubesatAttitudeQuaternionPointingNode> findById(String id, String tenantId) {
        CubesatAttitudeQuaternionPointingNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
