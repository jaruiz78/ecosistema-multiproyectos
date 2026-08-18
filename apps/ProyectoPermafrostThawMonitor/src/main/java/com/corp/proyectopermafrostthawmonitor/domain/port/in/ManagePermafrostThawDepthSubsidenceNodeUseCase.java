package com.corp.proyectopermafrostthawmonitor.domain.port.in;

import com.corp.proyectopermafrostthawmonitor.domain.model.PermafrostThawDepthSubsidenceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePermafrostThawDepthSubsidenceNodeUseCase {
    PermafrostThawDepthSubsidenceNode createPermafrostThawDepthSubsidenceNode(String tenantId, String title, double value);
    Optional<PermafrostThawDepthSubsidenceNode> findPermafrostThawDepthSubsidenceNodeById(String id, String tenantId);
    PermafrostThawDepthSubsidenceNode processOptimization(String id, String tenantId);
}
