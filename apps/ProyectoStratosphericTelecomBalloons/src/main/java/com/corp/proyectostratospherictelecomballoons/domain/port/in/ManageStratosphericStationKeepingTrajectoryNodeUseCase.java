package com.corp.proyectostratospherictelecomballoons.domain.port.in;

import com.corp.proyectostratospherictelecomballoons.domain.model.StratosphericStationKeepingTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageStratosphericStationKeepingTrajectoryNodeUseCase {
    StratosphericStationKeepingTrajectoryNode createStratosphericStationKeepingTrajectoryNode(String tenantId, String title, double value);
    Optional<StratosphericStationKeepingTrajectoryNode> findStratosphericStationKeepingTrajectoryNodeById(String id, String tenantId);
    StratosphericStationKeepingTrajectoryNode processOptimization(String id, String tenantId);
}
