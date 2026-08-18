package com.corp.proyectocislunarspacelogistics.domain.port.in;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLagrangeTrajectoryNodeUseCase {
    LagrangeTrajectoryNode createLagrangeTrajectoryNode(String tenantId, String title, double value);
    Optional<LagrangeTrajectoryNode> findLagrangeTrajectoryNodeById(String id, String tenantId);
    LagrangeTrajectoryNode processOptimization(String id, String tenantId);
}
