package com.corp.proyectoautonomousmaritimefreighter.domain.port.in;

import com.corp.proyectoautonomousmaritimefreighter.domain.model.MassVesselCollisionAvoidanceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMassVesselCollisionAvoidanceNodeUseCase {
    MassVesselCollisionAvoidanceNode createMassVesselCollisionAvoidanceNode(String tenantId, String title, double value);
    Optional<MassVesselCollisionAvoidanceNode> findMassVesselCollisionAvoidanceNodeById(String id, String tenantId);
    MassVesselCollisionAvoidanceNode processOptimization(String id, String tenantId);
}
