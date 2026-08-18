package com.corp.proyectosinglecellspatialomics.domain.port.out;

import com.corp.proyectosinglecellspatialomics.domain.model.SpatialTranscriptomeCellSpotNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpatialTranscriptomeCellSpotNodeRepositoryPort {
    SpatialTranscriptomeCellSpotNode save(SpatialTranscriptomeCellSpotNode entity);
    Optional<SpatialTranscriptomeCellSpotNode> findById(String id, String tenantId);
}
