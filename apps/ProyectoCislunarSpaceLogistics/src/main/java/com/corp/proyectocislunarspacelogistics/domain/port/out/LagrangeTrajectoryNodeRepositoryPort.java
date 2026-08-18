package com.corp.proyectocislunarspacelogistics.domain.port.out;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LagrangeTrajectoryNodeRepositoryPort {
    LagrangeTrajectoryNode save(LagrangeTrajectoryNode entity);
    Optional<LagrangeTrajectoryNode> findById(String id, String tenantId);
}
