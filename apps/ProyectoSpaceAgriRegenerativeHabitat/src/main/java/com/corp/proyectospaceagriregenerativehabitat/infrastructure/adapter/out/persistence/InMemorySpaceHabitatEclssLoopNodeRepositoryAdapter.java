package com.corp.proyectospaceagriregenerativehabitat.infrastructure.adapter.out.persistence;

import com.corp.proyectospaceagriregenerativehabitat.domain.model.SpaceHabitatEclssLoopNode;
import com.corp.proyectospaceagriregenerativehabitat.domain.port.out.SpaceHabitatEclssLoopNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySpaceHabitatEclssLoopNodeRepositoryAdapter implements SpaceHabitatEclssLoopNodeRepositoryPort {

    private final ConcurrentMap<String, SpaceHabitatEclssLoopNode> storage = new ConcurrentHashMap<>();

    @Override
    public SpaceHabitatEclssLoopNode save(SpaceHabitatEclssLoopNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpaceHabitatEclssLoopNode> findById(String id, String tenantId) {
        SpaceHabitatEclssLoopNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
