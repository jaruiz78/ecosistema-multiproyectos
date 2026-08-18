package com.corp.proyectocgraspatialaccelerator.domain.port.in;

import com.corp.proyectocgraspatialaccelerator.domain.model.CgraComputeMeshTileNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCgraComputeMeshTileNodeUseCase {
    CgraComputeMeshTileNode createCgraComputeMeshTileNode(String tenantId, String title, double value);
    Optional<CgraComputeMeshTileNode> findCgraComputeMeshTileNodeById(String id, String tenantId);
    CgraComputeMeshTileNode processOptimization(String id, String tenantId);
}
