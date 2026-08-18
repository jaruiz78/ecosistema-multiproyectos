package com.corp.proyectoautonomousoceangliders.domain.port.in;

import com.corp.proyectoautonomousoceangliders.domain.model.AuvSubmarineTrackWaypointNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAuvSubmarineTrackWaypointNodeUseCase {
    AuvSubmarineTrackWaypointNode createAuvSubmarineTrackWaypointNode(String tenantId, String title, double value);
    Optional<AuvSubmarineTrackWaypointNode> findAuvSubmarineTrackWaypointNodeById(String id, String tenantId);
    AuvSubmarineTrackWaypointNode processOptimization(String id, String tenantId);
}
