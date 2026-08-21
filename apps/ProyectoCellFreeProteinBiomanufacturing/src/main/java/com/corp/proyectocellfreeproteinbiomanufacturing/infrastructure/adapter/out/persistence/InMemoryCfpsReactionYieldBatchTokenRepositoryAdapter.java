package com.corp.proyectocellfreeproteinbiomanufacturing.infrastructure.adapter.out.persistence;

import com.corp.proyectocellfreeproteinbiomanufacturing.domain.model.CfpsReactionYieldBatchToken;
import com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.out.CfpsReactionYieldBatchTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCfpsReactionYieldBatchTokenRepositoryAdapter implements CfpsReactionYieldBatchTokenRepositoryPort {

    private final ConcurrentMap<String, CfpsReactionYieldBatchToken> storage = new ConcurrentHashMap<>();

    @Override
    public CfpsReactionYieldBatchToken save(CfpsReactionYieldBatchToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CfpsReactionYieldBatchToken> findById(String id, String tenantId) {
        CfpsReactionYieldBatchToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
