package com.corp.proyectovolcanicashairspacesafety.domain.port.in;

import com.corp.proyectovolcanicashairspacesafety.domain.model.VolcanicAshConcentrationFlightLevelNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageVolcanicAshConcentrationFlightLevelNodeUseCase {
    VolcanicAshConcentrationFlightLevelNode createVolcanicAshConcentrationFlightLevelNode(String tenantId, String title, double value);
    Optional<VolcanicAshConcentrationFlightLevelNode> findVolcanicAshConcentrationFlightLevelNodeById(String id, String tenantId);
    VolcanicAshConcentrationFlightLevelNode processOptimization(String id, String tenantId);
}
