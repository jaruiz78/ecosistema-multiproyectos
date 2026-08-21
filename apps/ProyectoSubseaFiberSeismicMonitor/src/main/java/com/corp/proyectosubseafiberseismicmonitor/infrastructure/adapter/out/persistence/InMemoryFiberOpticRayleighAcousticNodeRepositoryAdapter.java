package com.corp.proyectosubseafiberseismicmonitor.infrastructure.adapter.out.persistence;

import com.corp.proyectosubseafiberseismicmonitor.domain.model.FiberOpticRayleighAcousticNode;
import com.corp.proyectosubseafiberseismicmonitor.domain.port.out.FiberOpticRayleighAcousticNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryFiberOpticRayleighAcousticNodeRepositoryAdapter implements FiberOpticRayleighAcousticNodeRepositoryPort {

    private final ConcurrentMap<String, FiberOpticRayleighAcousticNode> storage = new ConcurrentHashMap<>();

    @Override
    public FiberOpticRayleighAcousticNode save(FiberOpticRayleighAcousticNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<FiberOpticRayleighAcousticNode> findById(String id, String tenantId) {
        FiberOpticRayleighAcousticNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
