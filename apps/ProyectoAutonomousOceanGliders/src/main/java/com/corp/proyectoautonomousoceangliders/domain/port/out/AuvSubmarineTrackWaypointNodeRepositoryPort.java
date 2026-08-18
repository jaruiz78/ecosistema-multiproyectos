package com.corp.proyectoautonomousoceangliders.domain.port.out;

import com.corp.proyectoautonomousoceangliders.domain.model.AuvSubmarineTrackWaypointNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AuvSubmarineTrackWaypointNodeRepositoryPort {
    AuvSubmarineTrackWaypointNode save(AuvSubmarineTrackWaypointNode entity);
    Optional<AuvSubmarineTrackWaypointNode> findById(String id, String tenantId);
}
