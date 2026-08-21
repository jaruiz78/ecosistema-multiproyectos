package com.corp.proyectooceanacidificationpreserve.infrastructure.adapter.out.persistence;

import com.corp.proyectooceanacidificationpreserve.domain.model.AragoniteSaturationStateOmegaNode;
import com.corp.proyectooceanacidificationpreserve.domain.port.out.AragoniteSaturationStateOmegaNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAragoniteSaturationStateOmegaNodeRepositoryAdapter implements AragoniteSaturationStateOmegaNodeRepositoryPort {

    private final ConcurrentMap<String, AragoniteSaturationStateOmegaNode> storage = new ConcurrentHashMap<>();

    @Override
    public AragoniteSaturationStateOmegaNode save(AragoniteSaturationStateOmegaNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AragoniteSaturationStateOmegaNode> findById(String id, String tenantId) {
        AragoniteSaturationStateOmegaNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
