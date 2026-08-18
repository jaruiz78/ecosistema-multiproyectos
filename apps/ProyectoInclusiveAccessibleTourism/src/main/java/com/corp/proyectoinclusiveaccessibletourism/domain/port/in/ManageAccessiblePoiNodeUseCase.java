package com.corp.proyectoinclusiveaccessibletourism.domain.port.in;

import com.corp.proyectoinclusiveaccessibletourism.domain.model.AccessiblePoiNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAccessiblePoiNodeUseCase {
    AccessiblePoiNode createAccessiblePoiNode(String tenantId, String title, double value);
    Optional<AccessiblePoiNode> findAccessiblePoiNodeById(String id, String tenantId);
    AccessiblePoiNode processOptimization(String id, String tenantId);
}
