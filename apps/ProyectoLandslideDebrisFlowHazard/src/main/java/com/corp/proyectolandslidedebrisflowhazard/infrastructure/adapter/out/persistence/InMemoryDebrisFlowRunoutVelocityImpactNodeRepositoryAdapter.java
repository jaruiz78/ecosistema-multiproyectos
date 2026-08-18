package com.corp.proyectolandslidedebrisflowhazard.infrastructure.adapter.out.persistence;

import com.corp.proyectolandslidedebrisflowhazard.domain.model.DebrisFlowRunoutVelocityImpactNode;
import com.corp.proyectolandslidedebrisflowhazard.domain.port.out.DebrisFlowRunoutVelocityImpactNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDebrisFlowRunoutVelocityImpactNodeRepositoryAdapter implements DebrisFlowRunoutVelocityImpactNodeRepositoryPort {

    private final ConcurrentMap<String, DebrisFlowRunoutVelocityImpactNode> storage = new ConcurrentHashMap<>();

    @Override
    public DebrisFlowRunoutVelocityImpactNode save(DebrisFlowRunoutVelocityImpactNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DebrisFlowRunoutVelocityImpactNode> findById(String id, String tenantId) {
        DebrisFlowRunoutVelocityImpactNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
