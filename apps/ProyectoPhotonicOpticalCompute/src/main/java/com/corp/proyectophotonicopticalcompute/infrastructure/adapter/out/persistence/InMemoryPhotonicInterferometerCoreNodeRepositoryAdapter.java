package com.corp.proyectophotonicopticalcompute.infrastructure.adapter.out.persistence;

import com.corp.proyectophotonicopticalcompute.domain.model.PhotonicInterferometerCoreNode;
import com.corp.proyectophotonicopticalcompute.domain.port.out.PhotonicInterferometerCoreNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPhotonicInterferometerCoreNodeRepositoryAdapter implements PhotonicInterferometerCoreNodeRepositoryPort {

    private final ConcurrentMap<String, PhotonicInterferometerCoreNode> storage = new ConcurrentHashMap<>();

    @Override
    public PhotonicInterferometerCoreNode save(PhotonicInterferometerCoreNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PhotonicInterferometerCoreNode> findById(String id, String tenantId) {
        PhotonicInterferometerCoreNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
