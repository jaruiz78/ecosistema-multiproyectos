package com.corp.proyectostratospherictelecomballoons.domain.port.out;

import com.corp.proyectostratospherictelecomballoons.domain.model.StratosphericStationKeepingTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface StratosphericStationKeepingTrajectoryNodeRepositoryPort {
    StratosphericStationKeepingTrajectoryNode save(StratosphericStationKeepingTrajectoryNode entity);
    Optional<StratosphericStationKeepingTrajectoryNode> findById(String id, String tenantId);
}
