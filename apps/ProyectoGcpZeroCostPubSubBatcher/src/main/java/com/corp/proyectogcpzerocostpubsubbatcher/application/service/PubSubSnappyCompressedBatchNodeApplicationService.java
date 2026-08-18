package com.corp.proyectogcpzerocostpubsubbatcher.application.service;

import com.corp.proyectogcpzerocostpubsubbatcher.domain.model.PubSubSnappyCompressedBatchNode;
import com.corp.proyectogcpzerocostpubsubbatcher.domain.port.in.ManagePubSubSnappyCompressedBatchNodeUseCase;
import com.corp.proyectogcpzerocostpubsubbatcher.domain.port.out.PubSubSnappyCompressedBatchNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PubSubSnappyCompressedBatchNode.
 */
@Service
public class PubSubSnappyCompressedBatchNodeApplicationService implements ManagePubSubSnappyCompressedBatchNodeUseCase {

    private final PubSubSnappyCompressedBatchNodeRepositoryPort repositoryPort;

    public PubSubSnappyCompressedBatchNodeApplicationService(PubSubSnappyCompressedBatchNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PubSubSnappyCompressedBatchNode createPubSubSnappyCompressedBatchNode(String tenantId, String title, double value) {
        PubSubSnappyCompressedBatchNode entity = new PubSubSnappyCompressedBatchNode(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<PubSubSnappyCompressedBatchNode> findPubSubSnappyCompressedBatchNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PubSubSnappyCompressedBatchNode processOptimization(String id, String tenantId) {
        PubSubSnappyCompressedBatchNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PubSubSnappyCompressedBatchNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
