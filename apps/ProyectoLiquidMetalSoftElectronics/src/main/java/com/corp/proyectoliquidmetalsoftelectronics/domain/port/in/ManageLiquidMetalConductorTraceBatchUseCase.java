package com.corp.proyectoliquidmetalsoftelectronics.domain.port.in;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLiquidMetalConductorTraceBatchUseCase {
    LiquidMetalConductorTraceBatch createLiquidMetalConductorTraceBatch(String tenantId, String title, double value);
    Optional<LiquidMetalConductorTraceBatch> findLiquidMetalConductorTraceBatchById(String id, String tenantId);
    LiquidMetalConductorTraceBatch processOptimization(String id, String tenantId);
}
