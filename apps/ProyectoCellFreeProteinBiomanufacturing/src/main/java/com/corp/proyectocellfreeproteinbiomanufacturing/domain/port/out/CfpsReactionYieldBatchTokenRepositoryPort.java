package com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.out;

import com.corp.proyectocellfreeproteinbiomanufacturing.domain.model.CfpsReactionYieldBatchToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CfpsReactionYieldBatchTokenRepositoryPort {
    CfpsReactionYieldBatchToken save(CfpsReactionYieldBatchToken entity);
    Optional<CfpsReactionYieldBatchToken> findById(String id, String tenantId);
}
