package com.corp.proyectolandslidedebrisflowhazard.domain.port.in;

import com.corp.proyectolandslidedebrisflowhazard.domain.model.DebrisFlowRunoutVelocityImpactNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDebrisFlowRunoutVelocityImpactNodeUseCase {
    DebrisFlowRunoutVelocityImpactNode createDebrisFlowRunoutVelocityImpactNode(String tenantId, String title, double value);
    Optional<DebrisFlowRunoutVelocityImpactNode> findDebrisFlowRunoutVelocityImpactNodeById(String id, String tenantId);
    DebrisFlowRunoutVelocityImpactNode processOptimization(String id, String tenantId);
}
