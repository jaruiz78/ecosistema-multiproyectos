package com.corp.proyectometabolicoptknockengineering.domain.port.in;

import com.corp.proyectometabolicoptknockengineering.domain.model.GeneDeletionTargetVectorToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGeneDeletionTargetVectorTokenUseCase {
    GeneDeletionTargetVectorToken createGeneDeletionTargetVectorToken(String tenantId, String title, double value);
    Optional<GeneDeletionTargetVectorToken> findGeneDeletionTargetVectorTokenById(String id, String tenantId);
    GeneDeletionTargetVectorToken processOptimization(String id, String tenantId);
}
