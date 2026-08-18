package com.corp.proyectolitertedgeinferencehub.domain.port.in;

import com.corp.proyectolitertedgeinferencehub.domain.model.LiteRtQuantizedModelExecutionNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLiteRtQuantizedModelExecutionNodeUseCase {
    LiteRtQuantizedModelExecutionNode createLiteRtQuantizedModelExecutionNode(String tenantId, String title, double value);
    Optional<LiteRtQuantizedModelExecutionNode> findLiteRtQuantizedModelExecutionNodeById(String id, String tenantId);
    LiteRtQuantizedModelExecutionNode processOptimization(String id, String tenantId);
}
