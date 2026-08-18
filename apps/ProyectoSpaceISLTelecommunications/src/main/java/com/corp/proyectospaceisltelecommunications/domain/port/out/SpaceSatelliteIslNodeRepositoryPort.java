package com.corp.proyectospaceisltelecommunications.domain.port.out;

import com.corp.proyectospaceisltelecommunications.domain.model.SpaceSatelliteIslNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpaceSatelliteIslNodeRepositoryPort {
    SpaceSatelliteIslNode save(SpaceSatelliteIslNode entity);
    Optional<SpaceSatelliteIslNode> findById(String id, String tenantId);
}
