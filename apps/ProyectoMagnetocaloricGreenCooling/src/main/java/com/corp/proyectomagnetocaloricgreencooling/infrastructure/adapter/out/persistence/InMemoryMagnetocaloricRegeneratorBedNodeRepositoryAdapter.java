package com.corp.proyectomagnetocaloricgreencooling.infrastructure.adapter.out.persistence;

import com.corp.proyectomagnetocaloricgreencooling.domain.model.MagnetocaloricRegeneratorBedNode;
import com.corp.proyectomagnetocaloricgreencooling.domain.port.out.MagnetocaloricRegeneratorBedNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMagnetocaloricRegeneratorBedNodeRepositoryAdapter implements MagnetocaloricRegeneratorBedNodeRepositoryPort {

    private final ConcurrentMap<String, MagnetocaloricRegeneratorBedNode> storage = new ConcurrentHashMap<>();

    @Override
    public MagnetocaloricRegeneratorBedNode save(MagnetocaloricRegeneratorBedNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MagnetocaloricRegeneratorBedNode> findById(String id, String tenantId) {
        MagnetocaloricRegeneratorBedNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
