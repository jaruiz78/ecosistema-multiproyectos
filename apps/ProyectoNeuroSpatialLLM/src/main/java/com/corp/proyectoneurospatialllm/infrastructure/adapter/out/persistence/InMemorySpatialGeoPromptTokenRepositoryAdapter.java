package com.corp.proyectoneurospatialllm.infrastructure.adapter.out.persistence;

import com.corp.proyectoneurospatialllm.domain.model.SpatialGeoPromptToken;
import com.corp.proyectoneurospatialllm.domain.port.out.SpatialGeoPromptTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySpatialGeoPromptTokenRepositoryAdapter implements SpatialGeoPromptTokenRepositoryPort {

    private final ConcurrentMap<String, SpatialGeoPromptToken> storage = new ConcurrentHashMap<>();

    @Override
    public SpatialGeoPromptToken save(SpatialGeoPromptToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpatialGeoPromptToken> findById(String id, String tenantId) {
        SpatialGeoPromptToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
