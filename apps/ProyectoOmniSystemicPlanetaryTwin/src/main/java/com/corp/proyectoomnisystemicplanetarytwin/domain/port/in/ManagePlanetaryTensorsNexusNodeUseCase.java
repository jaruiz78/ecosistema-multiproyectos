package com.corp.proyectoomnisystemicplanetarytwin.domain.port.in;

import com.corp.proyectoomnisystemicplanetarytwin.domain.model.PlanetaryTensorsNexusNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePlanetaryTensorsNexusNodeUseCase {
    PlanetaryTensorsNexusNode createPlanetaryTensorsNexusNode(String tenantId, String title, double value);
    Optional<PlanetaryTensorsNexusNode> findPlanetaryTensorsNexusNodeById(String id, String tenantId);
    PlanetaryTensorsNexusNode processOptimization(String id, String tenantId);
}
