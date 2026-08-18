package com.corp.proyectogcpzerocostpubsubbatcher.infrastructure.adapter.out.persistence;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import com.corp.proyectogcpzerocostpubsubbatcher.domain.port.out.PubSubSnappyCompressedBatchNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
