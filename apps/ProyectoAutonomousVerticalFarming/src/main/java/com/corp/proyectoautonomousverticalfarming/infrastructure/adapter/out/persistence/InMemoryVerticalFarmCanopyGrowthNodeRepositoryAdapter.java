package com.corp.proyectoautonomousverticalfarming.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousverticalfarming.domain.model.VerticalFarmCanopyGrowthNode;
import com.corp.proyectoautonomousverticalfarming.domain.port.out.VerticalFarmCanopyGrowthNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryVerticalFarmCanopyGrowthNodeRepositoryAdapter implements VerticalFarmCanopyGrowthNodeRepositoryPort {

    private final ConcurrentMap<String, VerticalFarmCanopyGrowthNode> storage = new ConcurrentHashMap<>();

    @Override
    public VerticalFarmCanopyGrowthNode save(VerticalFarmCanopyGrowthNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VerticalFarmCanopyGrowthNode> findById(String id, String tenantId) {
        VerticalFarmCanopyGrowthNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
