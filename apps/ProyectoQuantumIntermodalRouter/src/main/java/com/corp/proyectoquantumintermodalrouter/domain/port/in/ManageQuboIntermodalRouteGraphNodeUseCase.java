package com.corp.proyectoquantumintermodalrouter.domain.port.in;

import com.corp.proyectoquantumintermodalrouter.domain.model.QuboIntermodalRouteGraphNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQuboIntermodalRouteGraphNodeUseCase {
    QuboIntermodalRouteGraphNode createQuboIntermodalRouteGraphNode(String tenantId, String title, double value);
    Optional<QuboIntermodalRouteGraphNode> findQuboIntermodalRouteGraphNodeById(String id, String tenantId);
    QuboIntermodalRouteGraphNode processOptimization(String id, String tenantId);
}
