package com.corp.proyectoomniplanetarymastertwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoomniplanetarymastertwin.domain.model.OmniPlanetaryTensorGraphNexusNode;
import com.corp.proyectoomniplanetarymastertwin.domain.port.out.OmniPlanetaryTensorGraphNexusNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryOmniPlanetaryTensorGraphNexusNodeRepositoryAdapter implements OmniPlanetaryTensorGraphNexusNodeRepositoryPort {

    private final ConcurrentMap<String, OmniPlanetaryTensorGraphNexusNode> storage = new ConcurrentHashMap<>();

    @Override
    public OmniPlanetaryTensorGraphNexusNode save(OmniPlanetaryTensorGraphNexusNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<OmniPlanetaryTensorGraphNexusNode> findById(String id, String tenantId) {
        OmniPlanetaryTensorGraphNexusNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
