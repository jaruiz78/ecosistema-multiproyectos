package com.corp.proyectosinglecellspatialomics.application.service;

import com.corp.proyectosinglecellspatialomics.domain.model.SpatialTranscriptomeCellSpotNode;
import com.corp.proyectosinglecellspatialomics.domain.port.in.ManageSpatialTranscriptomeCellSpotNodeUseCase;
import com.corp.proyectosinglecellspatialomics.domain.port.out.SpatialTranscriptomeCellSpotNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpatialTranscriptomeCellSpotNode.
 */
@Service
public class SpatialTranscriptomeCellSpotNodeApplicationService implements ManageSpatialTranscriptomeCellSpotNodeUseCase {

    private final SpatialTranscriptomeCellSpotNodeRepositoryPort repositoryPort;

    public SpatialTranscriptomeCellSpotNodeApplicationService(SpatialTranscriptomeCellSpotNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpatialTranscriptomeCellSpotNode createSpatialTranscriptomeCellSpotNode(String tenantId, String title, double value) {
        SpatialTranscriptomeCellSpotNode entity = new SpatialTranscriptomeCellSpotNode(
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
    public Optional<SpatialTranscriptomeCellSpotNode> findSpatialTranscriptomeCellSpotNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpatialTranscriptomeCellSpotNode processOptimization(String id, String tenantId) {
        SpatialTranscriptomeCellSpotNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpatialTranscriptomeCellSpotNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
