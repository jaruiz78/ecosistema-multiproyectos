package com.corp.proyectoquantumsatellitesar.domain.port.in;

import com.corp.proyectoquantumsatellitesar.domain.model.InSarDisplacementTrackNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageInSarDisplacementTrackNodeUseCase {
    InSarDisplacementTrackNode createInSarDisplacementTrackNode(String tenantId, String title, double value);
    Optional<InSarDisplacementTrackNode> findInSarDisplacementTrackNodeById(String id, String tenantId);
    InSarDisplacementTrackNode processOptimization(String id, String tenantId);
}
