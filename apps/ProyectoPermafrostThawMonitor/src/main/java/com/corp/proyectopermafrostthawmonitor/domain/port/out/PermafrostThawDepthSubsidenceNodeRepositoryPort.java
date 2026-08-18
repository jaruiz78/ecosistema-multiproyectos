package com.corp.proyectopermafrostthawmonitor.domain.port.out;

import com.corp.proyectopermafrostthawmonitor.domain.model.PermafrostThawDepthSubsidenceNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PermafrostThawDepthSubsidenceNodeRepositoryPort {
    PermafrostThawDepthSubsidenceNode save(PermafrostThawDepthSubsidenceNode entity);
    Optional<PermafrostThawDepthSubsidenceNode> findById(String id, String tenantId);
}
