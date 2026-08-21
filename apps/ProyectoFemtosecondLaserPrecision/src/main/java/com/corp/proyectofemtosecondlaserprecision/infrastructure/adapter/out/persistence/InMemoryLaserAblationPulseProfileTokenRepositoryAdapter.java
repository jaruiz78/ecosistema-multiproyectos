package com.corp.proyectofemtosecondlaserprecision.infrastructure.adapter.out.persistence;

import com.corp.proyectofemtosecondlaserprecision.domain.model.LaserAblationPulseProfileToken;
import com.corp.proyectofemtosecondlaserprecision.domain.port.out.LaserAblationPulseProfileTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryLaserAblationPulseProfileTokenRepositoryAdapter implements LaserAblationPulseProfileTokenRepositoryPort {

    private final ConcurrentMap<String, LaserAblationPulseProfileToken> storage = new ConcurrentHashMap<>();

    @Override
    public LaserAblationPulseProfileToken save(LaserAblationPulseProfileToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LaserAblationPulseProfileToken> findById(String id, String tenantId) {
        LaserAblationPulseProfileToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
