package com.corp.proyectosnowpackwaterresourcetwin.domain.port.out;

import com.corp.proyectosnowpackwaterresourcetwin.domain.model.SnowWaterEquivalentMeltRunoffNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SnowWaterEquivalentMeltRunoffNodeRepositoryPort {
    SnowWaterEquivalentMeltRunoffNode save(SnowWaterEquivalentMeltRunoffNode entity);
    Optional<SnowWaterEquivalentMeltRunoffNode> findById(String id, String tenantId);
}
