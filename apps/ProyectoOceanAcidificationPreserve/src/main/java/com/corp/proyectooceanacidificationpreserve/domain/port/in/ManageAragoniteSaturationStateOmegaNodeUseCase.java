package com.corp.proyectooceanacidificationpreserve.domain.port.in;

import com.corp.proyectooceanacidificationpreserve.domain.model.AragoniteSaturationStateOmegaNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAragoniteSaturationStateOmegaNodeUseCase {
    AragoniteSaturationStateOmegaNode createAragoniteSaturationStateOmegaNode(String tenantId, String title, double value);
    Optional<AragoniteSaturationStateOmegaNode> findAragoniteSaturationStateOmegaNodeById(String id, String tenantId);
    AragoniteSaturationStateOmegaNode processOptimization(String id, String tenantId);
}
