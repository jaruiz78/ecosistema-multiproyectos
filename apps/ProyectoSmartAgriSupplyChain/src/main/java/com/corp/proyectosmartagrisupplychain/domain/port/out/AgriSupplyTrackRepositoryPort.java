package com.corp.proyectosmartagrisupplychain.domain.port.out;

import com.corp.proyectosmartagrisupplychain.domain.model.AgriSupplyTrack;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AgriSupplyTrackRepositoryPort {
    AgriSupplyTrack save(AgriSupplyTrack entity);
    Optional<AgriSupplyTrack> findById(String id, String tenantId);
}
