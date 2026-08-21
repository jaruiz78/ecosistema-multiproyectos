package com.corp.proyectogcpzerocostpubsubbatcher.domain.port.in;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManagePubSubSnappyCompressedBatchNodeUseCase {
    PubSubSnappyCompressedBatchNode createPubSubSnappyCompressedBatchNode(String tenantId, String title, double value);
    Optional<PubSubSnappyCompressedBatchNode> findPubSubSnappyCompressedBatchNodeById(String id, String tenantId);
    PubSubSnappyCompressedBatchNode processOptimization(String id, String tenantId);
}
