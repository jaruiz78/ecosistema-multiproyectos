package com.corp.proyectosuborbitalspacetourismtwin.infrastructure.adapter.out.persistence;

import com.corp.proyectosuborbitalspacetourismtwin.domain.model.SpaceTourismPassengerGForceNode;
import com.corp.proyectosuborbitalspacetourismtwin.domain.port.out.SpaceTourismPassengerGForceNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySpaceTourismPassengerGForceNodeRepositoryAdapter implements SpaceTourismPassengerGForceNodeRepositoryPort {

    private final ConcurrentMap<String, SpaceTourismPassengerGForceNode> storage = new ConcurrentHashMap<>();

    @Override
    public SpaceTourismPassengerGForceNode save(SpaceTourismPassengerGForceNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpaceTourismPassengerGForceNode> findById(String id, String tenantId) {
        SpaceTourismPassengerGForceNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
