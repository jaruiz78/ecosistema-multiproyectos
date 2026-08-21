package com.corp.proyectogcpzerocostpubsubbatcher.infrastructure.adapter.out.persistence;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import com.corp.proyectogcpzerocostpubsubbatcher.domain.port.out.PubSubSnappyCompressedBatchNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPubSubSnappyCompressedBatchNodeRepositoryAdapter implements PubSubSnappyCompressedBatchNodeRepositoryPort {

    private final ConcurrentMap<String, PubSubSnappyCompressedBatchNode> storage = new ConcurrentHashMap<>();

    @Override
    public PubSubSnappyCompressedBatchNode save(PubSubSnappyCompressedBatchNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PubSubSnappyCompressedBatchNode> findById(String id, String tenantId) {
        PubSubSnappyCompressedBatchNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
