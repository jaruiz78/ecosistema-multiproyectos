package com.corp.proyectoquantumintermodalrouter.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumintermodalrouter.domain.model.QuboIntermodalRouteGraphNode;
import com.corp.proyectoquantumintermodalrouter.domain.port.out.QuboIntermodalRouteGraphNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryQuboIntermodalRouteGraphNodeRepositoryAdapter implements QuboIntermodalRouteGraphNodeRepositoryPort {

    private final ConcurrentMap<String, QuboIntermodalRouteGraphNode> storage = new ConcurrentHashMap<>();

    @Override
    public QuboIntermodalRouteGraphNode save(QuboIntermodalRouteGraphNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QuboIntermodalRouteGraphNode> findById(String id, String tenantId) {
        QuboIntermodalRouteGraphNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
