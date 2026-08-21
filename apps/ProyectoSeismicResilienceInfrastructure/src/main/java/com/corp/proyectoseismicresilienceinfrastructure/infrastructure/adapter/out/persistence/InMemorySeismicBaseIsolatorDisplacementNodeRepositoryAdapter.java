package com.corp.proyectoseismicresilienceinfrastructure.infrastructure.adapter.out.persistence;

import com.corp.proyectoseismicresilienceinfrastructure.domain.model.SeismicBaseIsolatorDisplacementNode;
import com.corp.proyectoseismicresilienceinfrastructure.domain.port.out.SeismicBaseIsolatorDisplacementNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySeismicBaseIsolatorDisplacementNodeRepositoryAdapter implements SeismicBaseIsolatorDisplacementNodeRepositoryPort {

    private final ConcurrentMap<String, SeismicBaseIsolatorDisplacementNode> storage = new ConcurrentHashMap<>();

    @Override
    public SeismicBaseIsolatorDisplacementNode save(SeismicBaseIsolatorDisplacementNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SeismicBaseIsolatorDisplacementNode> findById(String id, String tenantId) {
        SeismicBaseIsolatorDisplacementNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
