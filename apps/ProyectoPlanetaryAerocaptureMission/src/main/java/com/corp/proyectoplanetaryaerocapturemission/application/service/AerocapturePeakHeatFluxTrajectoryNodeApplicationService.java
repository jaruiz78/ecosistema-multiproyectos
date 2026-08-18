package com.corp.proyectoplanetaryaerocapturemission.application.service;

import com.corp.proyectoplanetaryaerocapturemission.domain.model.AerocapturePeakHeatFluxTrajectoryNode;
import com.corp.proyectoplanetaryaerocapturemission.domain.port.in.ManageAerocapturePeakHeatFluxTrajectoryNodeUseCase;
import com.corp.proyectoplanetaryaerocapturemission.domain.port.out.AerocapturePeakHeatFluxTrajectoryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AerocapturePeakHeatFluxTrajectoryNode.
 */
@Service
public class AerocapturePeakHeatFluxTrajectoryNodeApplicationService implements ManageAerocapturePeakHeatFluxTrajectoryNodeUseCase {

    private final AerocapturePeakHeatFluxTrajectoryNodeRepositoryPort repositoryPort;

    public AerocapturePeakHeatFluxTrajectoryNodeApplicationService(AerocapturePeakHeatFluxTrajectoryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AerocapturePeakHeatFluxTrajectoryNode createAerocapturePeakHeatFluxTrajectoryNode(String tenantId, String title, double value) {
        AerocapturePeakHeatFluxTrajectoryNode entity = new AerocapturePeakHeatFluxTrajectoryNode(
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
    public Optional<AerocapturePeakHeatFluxTrajectoryNode> findAerocapturePeakHeatFluxTrajectoryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AerocapturePeakHeatFluxTrajectoryNode processOptimization(String id, String tenantId) {
        AerocapturePeakHeatFluxTrajectoryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AerocapturePeakHeatFluxTrajectoryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
