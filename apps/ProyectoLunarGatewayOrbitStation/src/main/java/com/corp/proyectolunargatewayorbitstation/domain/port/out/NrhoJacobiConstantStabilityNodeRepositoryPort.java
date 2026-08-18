package com.corp.proyectolunargatewayorbitstation.domain.port.out;

import com.corp.proyectolunargatewayorbitstation.domain.model.NrhoJacobiConstantStabilityNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface NrhoJacobiConstantStabilityNodeRepositoryPort {
    NrhoJacobiConstantStabilityNode save(NrhoJacobiConstantStabilityNode entity);
    Optional<NrhoJacobiConstantStabilityNode> findById(String id, String tenantId);
}
