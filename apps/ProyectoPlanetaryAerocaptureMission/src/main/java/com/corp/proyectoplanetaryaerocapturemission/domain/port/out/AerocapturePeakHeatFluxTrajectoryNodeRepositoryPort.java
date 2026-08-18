package com.corp.proyectoplanetaryaerocapturemission.domain.port.out;

import com.corp.proyectoplanetaryaerocapturemission.domain.model.AerocapturePeakHeatFluxTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AerocapturePeakHeatFluxTrajectoryNodeRepositoryPort {
    AerocapturePeakHeatFluxTrajectoryNode save(AerocapturePeakHeatFluxTrajectoryNode entity);
    Optional<AerocapturePeakHeatFluxTrajectoryNode> findById(String id, String tenantId);
}
