package com.corp.proyectoplanetaryaerocapturemission.domain.port.in;

import com.corp.proyectoplanetaryaerocapturemission.domain.model.AerocapturePeakHeatFluxTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAerocapturePeakHeatFluxTrajectoryNodeUseCase {
    AerocapturePeakHeatFluxTrajectoryNode createAerocapturePeakHeatFluxTrajectoryNode(String tenantId, String title, double value);
    Optional<AerocapturePeakHeatFluxTrajectoryNode> findAerocapturePeakHeatFluxTrajectoryNodeById(String id, String tenantId);
    AerocapturePeakHeatFluxTrajectoryNode processOptimization(String id, String tenantId);
}
