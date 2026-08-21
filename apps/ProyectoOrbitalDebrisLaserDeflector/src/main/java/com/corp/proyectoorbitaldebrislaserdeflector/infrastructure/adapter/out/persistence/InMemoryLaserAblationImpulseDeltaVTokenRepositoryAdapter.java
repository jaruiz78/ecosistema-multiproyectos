package com.corp.proyectoorbitaldebrislaserdeflector.infrastructure.adapter.out.persistence;

import com.corp.proyectoorbitaldebrislaserdeflector.domain.model.LaserAblationImpulseDeltaVToken;
import com.corp.proyectoorbitaldebrislaserdeflector.domain.port.out.LaserAblationImpulseDeltaVTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryLaserAblationImpulseDeltaVTokenRepositoryAdapter implements LaserAblationImpulseDeltaVTokenRepositoryPort {

    private final ConcurrentMap<String, LaserAblationImpulseDeltaVToken> storage = new ConcurrentHashMap<>();

    @Override
    public LaserAblationImpulseDeltaVToken save(LaserAblationImpulseDeltaVToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LaserAblationImpulseDeltaVToken> findById(String id, String tenantId) {
        LaserAblationImpulseDeltaVToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
