package com.corp.proyectogcpzerocostpubsubbatcher.domain.port.in;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePubSubSnappyCompressedBatchNodeUseCase {
    PubSubSnappyCompressedBatchNode createPubSubSnappyCompressedBatchNode(String tenantId, String title, double value);
    Optional<PubSubSnappyCompressedBatchNode> findPubSubSnappyCompressedBatchNodeById(String id, String tenantId);
    PubSubSnappyCompressedBatchNode processOptimization(String id, String tenantId);
}
