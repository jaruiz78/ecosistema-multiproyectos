package com.corp.proyectoasteroidresourceprospector.infrastructure.adapter.out.persistence;

import com.corp.proyectoasteroidresourceprospector.domain.model.AsteroidMineralAbundanceScoreToken;
import com.corp.proyectoasteroidresourceprospector.domain.port.out.AsteroidMineralAbundanceScoreTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAsteroidMineralAbundanceScoreTokenRepositoryAdapter implements AsteroidMineralAbundanceScoreTokenRepositoryPort {

    private final ConcurrentMap<String, AsteroidMineralAbundanceScoreToken> storage = new ConcurrentHashMap<>();

    @Override
    public AsteroidMineralAbundanceScoreToken save(AsteroidMineralAbundanceScoreToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AsteroidMineralAbundanceScoreToken> findById(String id, String tenantId) {
        AsteroidMineralAbundanceScoreToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
