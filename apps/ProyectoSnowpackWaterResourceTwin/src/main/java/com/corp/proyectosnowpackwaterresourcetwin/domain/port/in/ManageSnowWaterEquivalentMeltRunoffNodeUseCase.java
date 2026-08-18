package com.corp.proyectosnowpackwaterresourcetwin.domain.port.in;

import com.corp.proyectosnowpackwaterresourcetwin.domain.model.SnowWaterEquivalentMeltRunoffNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSnowWaterEquivalentMeltRunoffNodeUseCase {
    SnowWaterEquivalentMeltRunoffNode createSnowWaterEquivalentMeltRunoffNode(String tenantId, String title, double value);
    Optional<SnowWaterEquivalentMeltRunoffNode> findSnowWaterEquivalentMeltRunoffNodeById(String id, String tenantId);
    SnowWaterEquivalentMeltRunoffNode processOptimization(String id, String tenantId);
}
