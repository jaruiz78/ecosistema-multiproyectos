package com.corp.proyectophotoniccrystalwaveguide.infrastructure.adapter.out.persistence;

import com.corp.proyectophotoniccrystalwaveguide.domain.model.PhotonicWaveguideCouplerNode;
import com.corp.proyectophotoniccrystalwaveguide.domain.port.out.PhotonicWaveguideCouplerNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPhotonicWaveguideCouplerNodeRepositoryAdapter implements PhotonicWaveguideCouplerNodeRepositoryPort {

    private final ConcurrentMap<String, PhotonicWaveguideCouplerNode> storage = new ConcurrentHashMap<>();

    @Override
    public PhotonicWaveguideCouplerNode save(PhotonicWaveguideCouplerNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PhotonicWaveguideCouplerNode> findById(String id, String tenantId) {
        PhotonicWaveguideCouplerNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
