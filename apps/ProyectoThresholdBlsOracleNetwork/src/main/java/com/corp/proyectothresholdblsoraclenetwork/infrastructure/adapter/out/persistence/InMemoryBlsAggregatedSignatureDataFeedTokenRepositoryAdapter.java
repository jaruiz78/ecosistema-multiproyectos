package com.corp.proyectothresholdblsoraclenetwork.infrastructure.adapter.out.persistence;

import com.corp.proyectothresholdblsoraclenetwork.domain.model.BlsAggregatedSignatureDataFeedToken;
import com.corp.proyectothresholdblsoraclenetwork.domain.port.out.BlsAggregatedSignatureDataFeedTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryBlsAggregatedSignatureDataFeedTokenRepositoryAdapter implements BlsAggregatedSignatureDataFeedTokenRepositoryPort {

    private final ConcurrentMap<String, BlsAggregatedSignatureDataFeedToken> storage = new ConcurrentHashMap<>();

    @Override
    public BlsAggregatedSignatureDataFeedToken save(BlsAggregatedSignatureDataFeedToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BlsAggregatedSignatureDataFeedToken> findById(String id, String tenantId) {
        BlsAggregatedSignatureDataFeedToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
