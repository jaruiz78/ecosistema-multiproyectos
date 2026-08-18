package com.corp.proyectoquantumsatellitesar.domain.port.out;

import com.corp.proyectoquantumsatellitesar.domain.model.InSarDisplacementTrackNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface InSarDisplacementTrackNodeRepositoryPort {
    InSarDisplacementTrackNode save(InSarDisplacementTrackNode entity);
    Optional<InSarDisplacementTrackNode> findById(String id, String tenantId);
}
