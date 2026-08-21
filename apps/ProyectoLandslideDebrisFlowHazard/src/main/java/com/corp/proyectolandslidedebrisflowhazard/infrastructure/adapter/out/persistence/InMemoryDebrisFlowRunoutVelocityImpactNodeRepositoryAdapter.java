package com.corp.proyectolandslidedebrisflowhazard.infrastructure.adapter.out.persistence;

import com.corp.proyectolandslidedebrisflowhazard.domain.model.DebrisFlowRunoutVelocityImpactNode;
import com.corp.proyectolandslidedebrisflowhazard.domain.port.out.DebrisFlowRunoutVelocityImpactNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
