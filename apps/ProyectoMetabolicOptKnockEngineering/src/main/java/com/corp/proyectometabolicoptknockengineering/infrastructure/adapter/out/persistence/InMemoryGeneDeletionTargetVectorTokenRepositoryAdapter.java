package com.corp.proyectometabolicoptknockengineering.infrastructure.adapter.out.persistence;

import com.corp.proyectometabolicoptknockengineering.domain.model.GeneDeletionTargetVectorToken;
import com.corp.proyectometabolicoptknockengineering.domain.port.out.GeneDeletionTargetVectorTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryGeneDeletionTargetVectorTokenRepositoryAdapter implements GeneDeletionTargetVectorTokenRepositoryPort {

    private final ConcurrentMap<String, GeneDeletionTargetVectorToken> storage = new ConcurrentHashMap<>();

    @Override
    public GeneDeletionTargetVectorToken save(GeneDeletionTargetVectorToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GeneDeletionTargetVectorToken> findById(String id, String tenantId) {
        GeneDeletionTargetVectorToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
