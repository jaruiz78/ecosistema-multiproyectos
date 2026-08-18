package com.corp.proyectoautonomousmaglevfreight.application.service;

import com.corp.proyectoautonomousmaglevfreight.domain.model.MaglevFreightTrainTrackNode;
import com.corp.proyectoautonomousmaglevfreight.domain.port.in.ManageMaglevFreightTrainTrackNodeUseCase;
import com.corp.proyectoautonomousmaglevfreight.domain.port.out.MaglevFreightTrainTrackNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MaglevFreightTrainTrackNode.
 */
@Service
public class MaglevFreightTrainTrackNodeApplicationService implements ManageMaglevFreightTrainTrackNodeUseCase {

    private final MaglevFreightTrainTrackNodeRepositoryPort repositoryPort;

    public MaglevFreightTrainTrackNodeApplicationService(MaglevFreightTrainTrackNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MaglevFreightTrainTrackNode createMaglevFreightTrainTrackNode(String tenantId, String title, double value) {
        MaglevFreightTrainTrackNode entity = new MaglevFreightTrainTrackNode(
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
    public Optional<MaglevFreightTrainTrackNode> findMaglevFreightTrainTrackNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MaglevFreightTrainTrackNode processOptimization(String id, String tenantId) {
        MaglevFreightTrainTrackNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MaglevFreightTrainTrackNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
