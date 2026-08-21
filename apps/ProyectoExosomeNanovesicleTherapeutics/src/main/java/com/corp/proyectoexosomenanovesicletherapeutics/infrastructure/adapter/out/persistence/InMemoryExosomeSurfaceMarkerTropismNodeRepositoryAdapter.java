package com.corp.proyectoexosomenanovesicletherapeutics.infrastructure.adapter.out.persistence;

import com.corp.proyectoexosomenanovesicletherapeutics.domain.model.ExosomeSurfaceMarkerTropismNode;
import com.corp.proyectoexosomenanovesicletherapeutics.domain.port.out.ExosomeSurfaceMarkerTropismNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryExosomeSurfaceMarkerTropismNodeRepositoryAdapter implements ExosomeSurfaceMarkerTropismNodeRepositoryPort {

    private final ConcurrentMap<String, ExosomeSurfaceMarkerTropismNode> storage = new ConcurrentHashMap<>();

    @Override
    public ExosomeSurfaceMarkerTropismNode save(ExosomeSurfaceMarkerTropismNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ExosomeSurfaceMarkerTropismNode> findById(String id, String tenantId) {
        ExosomeSurfaceMarkerTropismNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
