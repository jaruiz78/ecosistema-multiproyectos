package com.corp.proyectoomniplanetarymastertwin.domain.port.out;

import com.corp.proyectoomniplanetarymastertwin.domain.model.OmniPlanetaryTensorGraphNexusNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface OmniPlanetaryTensorGraphNexusNodeRepositoryPort {
    OmniPlanetaryTensorGraphNexusNode save(OmniPlanetaryTensorGraphNexusNode entity);
    Optional<OmniPlanetaryTensorGraphNexusNode> findById(String id, String tenantId);
}
