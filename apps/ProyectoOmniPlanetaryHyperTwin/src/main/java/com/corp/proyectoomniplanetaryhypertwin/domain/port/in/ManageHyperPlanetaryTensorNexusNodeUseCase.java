package com.corp.proyectoomniplanetaryhypertwin.domain.port.in;

import com.corp.proyectoomniplanetaryhypertwin.domain.model.HyperPlanetaryTensorNexusNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHyperPlanetaryTensorNexusNodeUseCase {
    HyperPlanetaryTensorNexusNode createHyperPlanetaryTensorNexusNode(String tenantId, String title, double value);
    Optional<HyperPlanetaryTensorNexusNode> findHyperPlanetaryTensorNexusNodeById(String id, String tenantId);
    HyperPlanetaryTensorNexusNode processOptimization(String id, String tenantId);
}
