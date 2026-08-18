package com.corp.proyectoautonomousmaglevfreight.domain.port.out;

import com.corp.proyectoautonomousmaglevfreight.domain.model.MaglevFreightTrainTrackNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MaglevFreightTrainTrackNodeRepositoryPort {
    MaglevFreightTrainTrackNode save(MaglevFreightTrainTrackNode entity);
    Optional<MaglevFreightTrainTrackNode> findById(String id, String tenantId);
}
