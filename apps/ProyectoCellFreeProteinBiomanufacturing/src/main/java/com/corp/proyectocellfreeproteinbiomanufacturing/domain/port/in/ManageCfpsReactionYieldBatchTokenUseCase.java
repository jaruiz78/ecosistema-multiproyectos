package com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.in;

import com.corp.proyectocellfreeproteinbiomanufacturing.domain.model.CfpsReactionYieldBatchToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCfpsReactionYieldBatchTokenUseCase {
    CfpsReactionYieldBatchToken createCfpsReactionYieldBatchToken(String tenantId, String title, double value);
    Optional<CfpsReactionYieldBatchToken> findCfpsReactionYieldBatchTokenById(String id, String tenantId);
    CfpsReactionYieldBatchToken processOptimization(String id, String tenantId);
}
