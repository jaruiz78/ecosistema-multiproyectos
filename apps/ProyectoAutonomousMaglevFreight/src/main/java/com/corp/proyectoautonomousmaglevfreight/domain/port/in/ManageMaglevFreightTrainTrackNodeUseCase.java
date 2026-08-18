package com.corp.proyectoautonomousmaglevfreight.domain.port.in;

import com.corp.proyectoautonomousmaglevfreight.domain.model.MaglevFreightTrainTrackNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMaglevFreightTrainTrackNodeUseCase {
    MaglevFreightTrainTrackNode createMaglevFreightTrainTrackNode(String tenantId, String title, double value);
    Optional<MaglevFreightTrainTrackNode> findMaglevFreightTrainTrackNodeById(String id, String tenantId);
    MaglevFreightTrainTrackNode processOptimization(String id, String tenantId);
}
