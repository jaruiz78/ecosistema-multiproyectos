package com.corp.proyectosatellitedeorbitdragsail.domain.port.in;

import com.corp.proyectosatellitedeorbitdragsail.domain.model.DragSailAreaToMassRatioNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDragSailAreaToMassRatioNodeUseCase {
    DragSailAreaToMassRatioNode createDragSailAreaToMassRatioNode(String tenantId, String title, double value);
    Optional<DragSailAreaToMassRatioNode> findDragSailAreaToMassRatioNodeById(String id, String tenantId);
    DragSailAreaToMassRatioNode processOptimization(String id, String tenantId);
}
