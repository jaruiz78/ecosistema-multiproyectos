package com.corp.proyectoomniplanetaryhypertwin.domain.port.out;

import com.corp.proyectoomniplanetaryhypertwin.domain.model.HyperPlanetaryTensorNexusNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HyperPlanetaryTensorNexusNodeRepositoryPort {
    HyperPlanetaryTensorNexusNode save(HyperPlanetaryTensorNexusNode entity);
    Optional<HyperPlanetaryTensorNexusNode> findById(String id, String tenantId);
}
