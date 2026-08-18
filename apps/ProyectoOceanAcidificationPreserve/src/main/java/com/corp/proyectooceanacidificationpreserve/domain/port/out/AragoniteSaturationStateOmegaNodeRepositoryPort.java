package com.corp.proyectooceanacidificationpreserve.domain.port.out;

import com.corp.proyectooceanacidificationpreserve.domain.model.AragoniteSaturationStateOmegaNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AragoniteSaturationStateOmegaNodeRepositoryPort {
    AragoniteSaturationStateOmegaNode save(AragoniteSaturationStateOmegaNode entity);
    Optional<AragoniteSaturationStateOmegaNode> findById(String id, String tenantId);
}
