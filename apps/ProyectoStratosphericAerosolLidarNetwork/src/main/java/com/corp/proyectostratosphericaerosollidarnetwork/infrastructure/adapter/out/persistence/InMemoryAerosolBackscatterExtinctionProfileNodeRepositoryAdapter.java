package com.corp.proyectostratosphericaerosollidarnetwork.infrastructure.adapter.out.persistence;

import com.corp.proyectostratosphericaerosollidarnetwork.domain.model.AerosolBackscatterExtinctionProfileNode;
import com.corp.proyectostratosphericaerosollidarnetwork.domain.port.out.AerosolBackscatterExtinctionProfileNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAerosolBackscatterExtinctionProfileNodeRepositoryAdapter implements AerosolBackscatterExtinctionProfileNodeRepositoryPort {

    private final ConcurrentMap<String, AerosolBackscatterExtinctionProfileNode> storage = new ConcurrentHashMap<>();

    @Override
    public AerosolBackscatterExtinctionProfileNode save(AerosolBackscatterExtinctionProfileNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AerosolBackscatterExtinctionProfileNode> findById(String id, String tenantId) {
        AerosolBackscatterExtinctionProfileNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
