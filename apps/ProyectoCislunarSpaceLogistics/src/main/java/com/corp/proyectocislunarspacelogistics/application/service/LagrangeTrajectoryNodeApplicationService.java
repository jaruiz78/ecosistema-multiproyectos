package com.corp.proyectocislunarspacelogistics.application.service;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTrajectoryNode;
import com.corp.proyectocislunarspacelogistics.domain.port.in.ManageLagrangeTrajectoryNodeUseCase;
import com.corp.proyectocislunarspacelogistics.domain.port.out.LagrangeTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LagrangeTrajectoryNode.
 */
@Service
public class LagrangeTrajectoryNodeApplicationService implements ManageLagrangeTrajectoryNodeUseCase {

    private final LagrangeTrajectoryNodeRepositoryPort repositoryPort;

    public LagrangeTrajectoryNodeApplicationService(LagrangeTrajectoryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LagrangeTrajectoryNode createLagrangeTrajectoryNode(String tenantId, String title, double value) {
        LagrangeTrajectoryNode entity = new LagrangeTrajectoryNode(
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
    public Optional<LagrangeTrajectoryNode> findLagrangeTrajectoryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LagrangeTrajectoryNode processOptimization(String id, String tenantId) {
        LagrangeTrajectoryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LagrangeTrajectoryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
