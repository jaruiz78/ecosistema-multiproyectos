package com.corp.proyectocontextcacheaiorchestrator.domain.port.out;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AiContextCacheSessionTokenRepositoryPort {
    AiContextCacheSessionToken save(AiContextCacheSessionToken entity);
    Optional<AiContextCacheSessionToken> findById(String id, String tenantId);
}
