package com.corp.proyectohydrodynamicdamfloodrouting.infrastructure.adapter.out.persistence;

import com.corp.proyectohydrodynamicdamfloodrouting.domain.model.FloodInundationGridCellNode;
import com.corp.proyectohydrodynamicdamfloodrouting.domain.port.out.FloodInundationGridCellNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryFloodInundationGridCellNodeRepositoryAdapter implements FloodInundationGridCellNodeRepositoryPort {

    private final ConcurrentMap<String, FloodInundationGridCellNode> storage = new ConcurrentHashMap<>();

    @Override
    public FloodInundationGridCellNode save(FloodInundationGridCellNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<FloodInundationGridCellNode> findById(String id, String tenantId) {
        FloodInundationGridCellNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
