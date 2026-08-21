package com.corp.proyectopermafrostthawmonitor.infrastructure.adapter.out.persistence;

import com.corp.proyectopermafrostthawmonitor.domain.model.PermafrostThawDepthSubsidenceNode;
import com.corp.proyectopermafrostthawmonitor.domain.port.out.PermafrostThawDepthSubsidenceNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPermafrostThawDepthSubsidenceNodeRepositoryAdapter implements PermafrostThawDepthSubsidenceNodeRepositoryPort {

    private final ConcurrentMap<String, PermafrostThawDepthSubsidenceNode> storage = new ConcurrentHashMap<>();

    @Override
    public PermafrostThawDepthSubsidenceNode save(PermafrostThawDepthSubsidenceNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PermafrostThawDepthSubsidenceNode> findById(String id, String tenantId) {
        PermafrostThawDepthSubsidenceNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
