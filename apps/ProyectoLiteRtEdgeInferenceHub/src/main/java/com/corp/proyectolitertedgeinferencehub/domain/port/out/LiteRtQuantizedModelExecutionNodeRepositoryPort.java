package com.corp.proyectolitertedgeinferencehub.domain.port.out;

import com.corp.proyectolitertedgeinferencehub.domain.model.LiteRtQuantizedModelExecutionNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LiteRtQuantizedModelExecutionNodeRepositoryPort {
    LiteRtQuantizedModelExecutionNode save(LiteRtQuantizedModelExecutionNode entity);
    Optional<LiteRtQuantizedModelExecutionNode> findById(String id, String tenantId);
}
