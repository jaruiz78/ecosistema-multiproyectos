package com.corp.proyectoundergroundfreighttubenetwork.infrastructure.adapter.out.persistence;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import com.corp.proyectoundergroundfreighttubenetwork.domain.port.out.UndergroundFreightCapsuleTrackNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryUndergroundFreightCapsuleTrackNodeRepositoryAdapter implements UndergroundFreightCapsuleTrackNodeRepositoryPort {

    private final ConcurrentMap<String, UndergroundFreightCapsuleTrackNode> storage = new ConcurrentHashMap<>();

    @Override
    public UndergroundFreightCapsuleTrackNode save(UndergroundFreightCapsuleTrackNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<UndergroundFreightCapsuleTrackNode> findById(String id, String tenantId) {
        UndergroundFreightCapsuleTrackNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
