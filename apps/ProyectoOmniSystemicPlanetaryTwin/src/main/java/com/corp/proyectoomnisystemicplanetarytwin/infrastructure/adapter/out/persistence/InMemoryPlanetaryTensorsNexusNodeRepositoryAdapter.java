package com.corp.proyectoomnisystemicplanetarytwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoomnisystemicplanetarytwin.domain.model.PlanetaryTensorsNexusNode;
import com.corp.proyectoomnisystemicplanetarytwin.domain.port.out.PlanetaryTensorsNexusNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPlanetaryTensorsNexusNodeRepositoryAdapter implements PlanetaryTensorsNexusNodeRepositoryPort {

    private final ConcurrentMap<String, PlanetaryTensorsNexusNode> storage = new ConcurrentHashMap<>();

    @Override
    public PlanetaryTensorsNexusNode save(PlanetaryTensorsNexusNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PlanetaryTensorsNexusNode> findById(String id, String tenantId) {
        PlanetaryTensorsNexusNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
