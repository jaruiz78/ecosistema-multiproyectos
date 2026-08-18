package com.corp.proyectolunargatewayorbitstation.domain.port.in;

import com.corp.proyectolunargatewayorbitstation.domain.model.NrhoJacobiConstantStabilityNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageNrhoJacobiConstantStabilityNodeUseCase {
    NrhoJacobiConstantStabilityNode createNrhoJacobiConstantStabilityNode(String tenantId, String title, double value);
    Optional<NrhoJacobiConstantStabilityNode> findNrhoJacobiConstantStabilityNodeById(String id, String tenantId);
    NrhoJacobiConstantStabilityNode processOptimization(String id, String tenantId);
}
