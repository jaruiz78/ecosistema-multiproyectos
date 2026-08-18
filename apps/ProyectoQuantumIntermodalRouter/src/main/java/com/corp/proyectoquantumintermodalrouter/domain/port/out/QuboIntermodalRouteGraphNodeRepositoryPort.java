package com.corp.proyectoquantumintermodalrouter.domain.port.out;

import com.corp.proyectoquantumintermodalrouter.domain.model.QuboIntermodalRouteGraphNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QuboIntermodalRouteGraphNodeRepositoryPort {
    QuboIntermodalRouteGraphNode save(QuboIntermodalRouteGraphNode entity);
    Optional<QuboIntermodalRouteGraphNode> findById(String id, String tenantId);
}
