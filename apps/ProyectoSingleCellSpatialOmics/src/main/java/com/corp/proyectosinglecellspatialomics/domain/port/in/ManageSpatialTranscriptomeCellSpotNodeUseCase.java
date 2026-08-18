package com.corp.proyectosinglecellspatialomics.domain.port.in;

import com.corp.proyectosinglecellspatialomics.domain.model.SpatialTranscriptomeCellSpotNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpatialTranscriptomeCellSpotNodeUseCase {
    SpatialTranscriptomeCellSpotNode createSpatialTranscriptomeCellSpotNode(String tenantId, String title, double value);
    Optional<SpatialTranscriptomeCellSpotNode> findSpatialTranscriptomeCellSpotNodeById(String id, String tenantId);
    SpatialTranscriptomeCellSpotNode processOptimization(String id, String tenantId);
}
