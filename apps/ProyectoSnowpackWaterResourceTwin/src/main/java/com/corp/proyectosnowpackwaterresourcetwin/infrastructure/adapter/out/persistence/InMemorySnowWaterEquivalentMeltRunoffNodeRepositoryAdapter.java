package com.corp.proyectosnowpackwaterresourcetwin.infrastructure.adapter.out.persistence;

import com.corp.proyectosnowpackwaterresourcetwin.domain.model.SnowWaterEquivalentMeltRunoffNode;
import com.corp.proyectosnowpackwaterresourcetwin.domain.port.out.SnowWaterEquivalentMeltRunoffNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySnowWaterEquivalentMeltRunoffNodeRepositoryAdapter implements SnowWaterEquivalentMeltRunoffNodeRepositoryPort {

    private final ConcurrentMap<String, SnowWaterEquivalentMeltRunoffNode> storage = new ConcurrentHashMap<>();

    @Override
    public SnowWaterEquivalentMeltRunoffNode save(SnowWaterEquivalentMeltRunoffNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SnowWaterEquivalentMeltRunoffNode> findById(String id, String tenantId) {
        SnowWaterEquivalentMeltRunoffNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
