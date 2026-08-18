package com.corp.proyectoreusablerocketlandingtwin.domain.port.out;

import com.corp.proyectoreusablerocketlandingtwin.domain.model.RocketRetroLandingTrajectoryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface RocketRetroLandingTrajectoryNodeRepositoryPort {
    RocketRetroLandingTrajectoryNode save(RocketRetroLandingTrajectoryNode entity);
    Optional<RocketRetroLandingTrajectoryNode> findById(String id, String tenantId);
}
