package com.corp.proyectosatellitedeorbitdragsail.infrastructure.adapter.out.persistence;

import com.corp.proyectosatellitedeorbitdragsail.domain.model.DragSailAreaToMassRatioNode;
import com.corp.proyectosatellitedeorbitdragsail.domain.port.out.DragSailAreaToMassRatioNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDragSailAreaToMassRatioNodeRepositoryAdapter implements DragSailAreaToMassRatioNodeRepositoryPort {

    private final ConcurrentMap<String, DragSailAreaToMassRatioNode> storage = new ConcurrentHashMap<>();

    @Override
    public DragSailAreaToMassRatioNode save(DragSailAreaToMassRatioNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DragSailAreaToMassRatioNode> findById(String id, String tenantId) {
        DragSailAreaToMassRatioNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
