package com.corp.proyectohighpowersolarelectrictug.infrastructure.adapter.out.persistence;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import com.corp.proyectohighpowersolarelectrictug.domain.port.out.SepXenonMassFlowThrustNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySepXenonMassFlowThrustNodeRepositoryAdapter implements SepXenonMassFlowThrustNodeRepositoryPort {

    private final ConcurrentMap<String, SepXenonMassFlowThrustNode> storage = new ConcurrentHashMap<>();

    @Override
    public SepXenonMassFlowThrustNode save(SepXenonMassFlowThrustNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SepXenonMassFlowThrustNode> findById(String id, String tenantId) {
        SepXenonMassFlowThrustNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
