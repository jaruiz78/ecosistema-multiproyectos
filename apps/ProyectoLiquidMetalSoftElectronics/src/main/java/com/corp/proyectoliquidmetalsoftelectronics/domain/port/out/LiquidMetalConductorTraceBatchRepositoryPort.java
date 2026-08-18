package com.corp.proyectoliquidmetalsoftelectronics.domain.port.out;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LiquidMetalConductorTraceBatchRepositoryPort {
    LiquidMetalConductorTraceBatch save(LiquidMetalConductorTraceBatch entity);
    Optional<LiquidMetalConductorTraceBatch> findById(String id, String tenantId);
}
