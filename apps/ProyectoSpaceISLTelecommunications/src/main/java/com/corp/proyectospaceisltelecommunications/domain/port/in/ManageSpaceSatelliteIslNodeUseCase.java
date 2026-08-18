package com.corp.proyectospaceisltelecommunications.domain.port.in;

import com.corp.proyectospaceisltelecommunications.domain.model.SpaceSatelliteIslNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpaceSatelliteIslNodeUseCase {
    SpaceSatelliteIslNode createSpaceSatelliteIslNode(String tenantId, String title, double value);
    Optional<SpaceSatelliteIslNode> findSpaceSatelliteIslNodeById(String id, String tenantId);
    SpaceSatelliteIslNode processOptimization(String id, String tenantId);
}
