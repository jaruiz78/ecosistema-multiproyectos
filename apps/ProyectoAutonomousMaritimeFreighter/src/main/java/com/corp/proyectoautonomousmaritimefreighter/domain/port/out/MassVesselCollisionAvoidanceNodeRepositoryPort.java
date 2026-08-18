package com.corp.proyectoautonomousmaritimefreighter.domain.port.out;

import com.corp.proyectoautonomousmaritimefreighter.domain.model.MassVesselCollisionAvoidanceNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MassVesselCollisionAvoidanceNodeRepositoryPort {
    MassVesselCollisionAvoidanceNode save(MassVesselCollisionAvoidanceNode entity);
    Optional<MassVesselCollisionAvoidanceNode> findById(String id, String tenantId);
}
