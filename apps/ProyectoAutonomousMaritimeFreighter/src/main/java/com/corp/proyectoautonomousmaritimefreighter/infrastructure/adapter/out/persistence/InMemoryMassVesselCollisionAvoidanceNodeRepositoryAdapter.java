package com.corp.proyectoautonomousmaritimefreighter.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousmaritimefreighter.domain.model.MassVesselCollisionAvoidanceNode;
import com.corp.proyectoautonomousmaritimefreighter.domain.port.out.MassVesselCollisionAvoidanceNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMassVesselCollisionAvoidanceNodeRepositoryAdapter implements MassVesselCollisionAvoidanceNodeRepositoryPort {

    private final ConcurrentMap<String, MassVesselCollisionAvoidanceNode> storage = new ConcurrentHashMap<>();

    @Override
    public MassVesselCollisionAvoidanceNode save(MassVesselCollisionAvoidanceNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MassVesselCollisionAvoidanceNode> findById(String id, String tenantId) {
        MassVesselCollisionAvoidanceNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
