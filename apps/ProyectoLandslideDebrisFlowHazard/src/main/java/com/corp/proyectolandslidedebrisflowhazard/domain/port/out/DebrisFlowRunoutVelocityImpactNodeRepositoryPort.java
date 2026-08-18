package com.corp.proyectolandslidedebrisflowhazard.domain.port.out;

import com.corp.proyectolandslidedebrisflowhazard.domain.model.DebrisFlowRunoutVelocityImpactNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DebrisFlowRunoutVelocityImpactNodeRepositoryPort {
    DebrisFlowRunoutVelocityImpactNode save(DebrisFlowRunoutVelocityImpactNode entity);
    Optional<DebrisFlowRunoutVelocityImpactNode> findById(String id, String tenantId);
}
