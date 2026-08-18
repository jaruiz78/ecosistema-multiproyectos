package com.corp.proyectosatellitedeorbitdragsail.domain.port.out;

import com.corp.proyectosatellitedeorbitdragsail.domain.model.DragSailAreaToMassRatioNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DragSailAreaToMassRatioNodeRepositoryPort {
    DragSailAreaToMassRatioNode save(DragSailAreaToMassRatioNode entity);
    Optional<DragSailAreaToMassRatioNode> findById(String id, String tenantId);
}
