package com.corp.proyectoinclusiveaccessibletourism.domain.port.out;

import com.corp.proyectoinclusiveaccessibletourism.domain.model.AccessiblePoiNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AccessiblePoiNodeRepositoryPort {
    AccessiblePoiNode save(AccessiblePoiNode entity);
    Optional<AccessiblePoiNode> findById(String id, String tenantId);
}
