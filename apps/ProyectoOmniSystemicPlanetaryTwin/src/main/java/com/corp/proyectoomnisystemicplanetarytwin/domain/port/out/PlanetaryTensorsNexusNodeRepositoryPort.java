package com.corp.proyectoomnisystemicplanetarytwin.domain.port.out;

import com.corp.proyectoomnisystemicplanetarytwin.domain.model.PlanetaryTensorsNexusNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PlanetaryTensorsNexusNodeRepositoryPort {
    PlanetaryTensorsNexusNode save(PlanetaryTensorsNexusNode entity);
    Optional<PlanetaryTensorsNexusNode> findById(String id, String tenantId);
}
