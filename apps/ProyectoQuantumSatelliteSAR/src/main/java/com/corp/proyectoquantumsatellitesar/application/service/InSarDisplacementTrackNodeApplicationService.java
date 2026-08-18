package com.corp.proyectoquantumsatellitesar.application.service;

import com.corp.proyectoquantumsatellitesar.domain.model.InSarDisplacementTrackNode;
import com.corp.proyectoquantumsatellitesar.domain.port.in.ManageInSarDisplacementTrackNodeUseCase;
import com.corp.proyectoquantumsatellitesar.domain.port.out.InSarDisplacementTrackNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de InSarDisplacementTrackNode.
 */
@Service
public class InSarDisplacementTrackNodeApplicationService implements ManageInSarDisplacementTrackNodeUseCase {

    private final InSarDisplacementTrackNodeRepositoryPort repositoryPort;

    public InSarDisplacementTrackNodeApplicationService(InSarDisplacementTrackNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public InSarDisplacementTrackNode createInSarDisplacementTrackNode(String tenantId, String title, double value) {
        InSarDisplacementTrackNode entity = new InSarDisplacementTrackNode(
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
    public Optional<InSarDisplacementTrackNode> findInSarDisplacementTrackNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public InSarDisplacementTrackNode processOptimization(String id, String tenantId) {
        InSarDisplacementTrackNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        InSarDisplacementTrackNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
