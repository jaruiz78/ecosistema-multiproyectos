package com.corp.proyectoreusablerocketlandingtwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoreusablerocketlandingtwin.domain.model.RocketRetroLandingTrajectoryNode;
import com.corp.proyectoreusablerocketlandingtwin.domain.port.out.RocketRetroLandingTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryRocketRetroLandingTrajectoryNodeRepositoryAdapter implements RocketRetroLandingTrajectoryNodeRepositoryPort {

    private final ConcurrentMap<String, RocketRetroLandingTrajectoryNode> storage = new ConcurrentHashMap<>();

    @Override
    public RocketRetroLandingTrajectoryNode save(RocketRetroLandingTrajectoryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<RocketRetroLandingTrajectoryNode> findById(String id, String tenantId) {
        RocketRetroLandingTrajectoryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
