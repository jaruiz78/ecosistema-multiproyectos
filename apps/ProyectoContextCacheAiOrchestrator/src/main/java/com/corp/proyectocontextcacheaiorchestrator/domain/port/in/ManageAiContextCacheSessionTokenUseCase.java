package com.corp.proyectocontextcacheaiorchestrator.domain.port.in;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAiContextCacheSessionTokenUseCase {
    AiContextCacheSessionToken createAiContextCacheSessionToken(String tenantId, String title, double value);
    Optional<AiContextCacheSessionToken> findAiContextCacheSessionTokenById(String id, String tenantId);
    AiContextCacheSessionToken processOptimization(String id, String tenantId);
}
