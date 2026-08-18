package com.corp.proyectoomniplanetarymastertwin.domain.port.in;

import com.corp.proyectoomniplanetarymastertwin.domain.model.OmniPlanetaryTensorGraphNexusNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageOmniPlanetaryTensorGraphNexusNodeUseCase {
    OmniPlanetaryTensorGraphNexusNode createOmniPlanetaryTensorGraphNexusNode(String tenantId, String title, double value);
    Optional<OmniPlanetaryTensorGraphNexusNode> findOmniPlanetaryTensorGraphNexusNodeById(String id, String tenantId);
    OmniPlanetaryTensorGraphNexusNode processOptimization(String id, String tenantId);
}
