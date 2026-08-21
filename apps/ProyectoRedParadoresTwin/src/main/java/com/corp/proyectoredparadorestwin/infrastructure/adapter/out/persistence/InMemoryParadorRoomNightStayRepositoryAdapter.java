package com.corp.proyectoredparadorestwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoredparadorestwin.domain.model.ParadorRoomNightStay;
import com.corp.proyectoredparadorestwin.domain.port.out.ParadorRoomNightStayRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryParadorRoomNightStayRepositoryAdapter implements ParadorRoomNightStayRepositoryPort {

    private final ConcurrentMap<String, ParadorRoomNightStay> storage = new ConcurrentHashMap<>();

    @Override
    public ParadorRoomNightStay save(ParadorRoomNightStay entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ParadorRoomNightStay> findById(String id, String tenantId) {
        ParadorRoomNightStay entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
