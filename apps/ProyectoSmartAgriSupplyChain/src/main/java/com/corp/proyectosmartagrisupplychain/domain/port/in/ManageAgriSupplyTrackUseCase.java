package com.corp.proyectosmartagrisupplychain.domain.port.in;

import com.corp.proyectosmartagrisupplychain.domain.model.AgriSupplyTrack;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAgriSupplyTrackUseCase {
    AgriSupplyTrack createAgriSupplyTrack(String tenantId, String title, double value);
    Optional<AgriSupplyTrack> findAgriSupplyTrackById(String id, String tenantId);
    AgriSupplyTrack processOptimization(String id, String tenantId);
}
