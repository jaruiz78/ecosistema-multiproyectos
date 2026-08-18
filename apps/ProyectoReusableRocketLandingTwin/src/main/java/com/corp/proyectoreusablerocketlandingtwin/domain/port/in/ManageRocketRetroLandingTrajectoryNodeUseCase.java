package com.corp.proyectoreusablerocketlandingtwin.domain.port.in;

import com.corp.proyectoreusablerocketlandingtwin.domain.model.RocketRetroLandingTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageRocketRetroLandingTrajectoryNodeUseCase {
    RocketRetroLandingTrajectoryNode createRocketRetroLandingTrajectoryNode(String tenantId, String title, double value);
    Optional<RocketRetroLandingTrajectoryNode> findRocketRetroLandingTrajectoryNodeById(String id, String tenantId);
    RocketRetroLandingTrajectoryNode processOptimization(String id, String tenantId);
}
