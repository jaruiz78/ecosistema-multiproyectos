package com.corp.proyectocgraspatialaccelerator.domain.port.out;

import com.corp.proyectocgraspatialaccelerator.domain.model.CgraComputeMeshTileNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CgraComputeMeshTileNodeRepositoryPort {
    CgraComputeMeshTileNode save(CgraComputeMeshTileNode entity);
    Optional<CgraComputeMeshTileNode> findById(String id, String tenantId);
}
