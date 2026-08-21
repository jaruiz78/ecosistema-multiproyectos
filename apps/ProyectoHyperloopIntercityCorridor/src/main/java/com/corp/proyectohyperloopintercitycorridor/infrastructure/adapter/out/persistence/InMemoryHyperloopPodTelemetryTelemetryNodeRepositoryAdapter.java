package com.corp.proyectohyperloopintercitycorridor.infrastructure.adapter.out.persistence;

import com.corp.proyectohyperloopintercitycorridor.domain.model.HyperloopPodTelemetryTelemetryNode;
import com.corp.proyectohyperloopintercitycorridor.domain.port.out.HyperloopPodTelemetryTelemetryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryHyperloopPodTelemetryTelemetryNodeRepositoryAdapter implements HyperloopPodTelemetryTelemetryNodeRepositoryPort {

    private final ConcurrentMap<String, HyperloopPodTelemetryTelemetryNode> storage = new ConcurrentHashMap<>();

    @Override
    public HyperloopPodTelemetryTelemetryNode save(HyperloopPodTelemetryTelemetryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HyperloopPodTelemetryTelemetryNode> findById(String id, String tenantId) {
        HyperloopPodTelemetryTelemetryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
