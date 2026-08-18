package com.corp.proyectometabolicoptknockengineering.domain.port.out;

import com.corp.proyectometabolicoptknockengineering.domain.model.GeneDeletionTargetVectorToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GeneDeletionTargetVectorTokenRepositoryPort {
    GeneDeletionTargetVectorToken save(GeneDeletionTargetVectorToken entity);
    Optional<GeneDeletionTargetVectorToken> findById(String id, String tenantId);
}
