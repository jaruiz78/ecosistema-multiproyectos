package com.corp.proyectomicrosatkabandsarconstellation.application.service;

import com.corp.proyectomicrosatkabandsarconstellation.domain.model.KaBandSarImageResolutionGridNode;
import com.corp.proyectomicrosatkabandsarconstellation.domain.port.in.ManageKaBandSarImageResolutionGridNodeUseCase;
import com.corp.proyectomicrosatkabandsarconstellation.domain.port.out.KaBandSarImageResolutionGridNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de KaBandSarImageResolutionGridNode.
 */
@Service
public class KaBandSarImageResolutionGridNodeApplicationService implements ManageKaBandSarImageResolutionGridNodeUseCase {

    private final KaBandSarImageResolutionGridNodeRepositoryPort repositoryPort;

    public KaBandSarImageResolutionGridNodeApplicationService(KaBandSarImageResolutionGridNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public KaBandSarImageResolutionGridNode createKaBandSarImageResolutionGridNode(String tenantId, String title, double value) {
        KaBandSarImageResolutionGridNode entity = new KaBandSarImageResolutionGridNode(
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
    public Optional<KaBandSarImageResolutionGridNode> findKaBandSarImageResolutionGridNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public KaBandSarImageResolutionGridNode processOptimization(String id, String tenantId) {
        KaBandSarImageResolutionGridNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        KaBandSarImageResolutionGridNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
