package com.corp.proyectostratospherictelecomballoons.application.service;

import com.corp.proyectostratospherictelecomballoons.domain.model.StratosphericStationKeepingTrajectoryNode;
import com.corp.proyectostratospherictelecomballoons.domain.port.in.ManageStratosphericStationKeepingTrajectoryNodeUseCase;
import com.corp.proyectostratospherictelecomballoons.domain.port.out.StratosphericStationKeepingTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de StratosphericStationKeepingTrajectoryNode.
 */
@Service
public class StratosphericStationKeepingTrajectoryNodeApplicationService implements ManageStratosphericStationKeepingTrajectoryNodeUseCase {

    private final StratosphericStationKeepingTrajectoryNodeRepositoryPort repositoryPort;

    public StratosphericStationKeepingTrajectoryNodeApplicationService(StratosphericStationKeepingTrajectoryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public StratosphericStationKeepingTrajectoryNode createStratosphericStationKeepingTrajectoryNode(String tenantId, String title, double value) {
        StratosphericStationKeepingTrajectoryNode entity = new StratosphericStationKeepingTrajectoryNode(
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
    public Optional<StratosphericStationKeepingTrajectoryNode> findStratosphericStationKeepingTrajectoryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public StratosphericStationKeepingTrajectoryNode processOptimization(String id, String tenantId) {
        StratosphericStationKeepingTrajectoryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        StratosphericStationKeepingTrajectoryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
