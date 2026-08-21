package com.corp.proyectocontextcacheaiorchestrator.infrastructure.adapter.out.persistence;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.out.AiContextCacheSessionTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAiContextCacheSessionTokenRepositoryAdapter implements AiContextCacheSessionTokenRepositoryPort {

    private final ConcurrentMap<String, AiContextCacheSessionToken> storage = new ConcurrentHashMap<>();

    @Override
    public AiContextCacheSessionToken save(AiContextCacheSessionToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AiContextCacheSessionToken> findById(String id, String tenantId) {
        AiContextCacheSessionToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
