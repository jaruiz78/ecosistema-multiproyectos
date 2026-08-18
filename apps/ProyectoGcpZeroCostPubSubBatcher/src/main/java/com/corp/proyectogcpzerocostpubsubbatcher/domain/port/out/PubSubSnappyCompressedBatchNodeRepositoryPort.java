package com.corp.proyectogcpzerocostpubsubbatcher.domain.port.out;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PubSubSnappyCompressedBatchNodeRepositoryPort {
    PubSubSnappyCompressedBatchNode save(PubSubSnappyCompressedBatchNode entity);
    Optional<PubSubSnappyCompressedBatchNode> findById(String id, String tenantId);
}
