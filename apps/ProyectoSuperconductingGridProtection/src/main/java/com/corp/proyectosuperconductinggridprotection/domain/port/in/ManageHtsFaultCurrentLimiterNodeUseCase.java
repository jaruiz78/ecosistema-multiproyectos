package com.corp.proyectosuperconductinggridprotection.domain.port.in;

import com.corp.proyectosuperconductinggridprotection.domain.model.HtsFaultCurrentLimiterNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHtsFaultCurrentLimiterNodeUseCase {
    HtsFaultCurrentLimiterNode createHtsFaultCurrentLimiterNode(String tenantId, String title, double value);
    Optional<HtsFaultCurrentLimiterNode> findHtsFaultCurrentLimiterNodeById(String id, String tenantId);
    HtsFaultCurrentLimiterNode processOptimization(String id, String tenantId);
}
