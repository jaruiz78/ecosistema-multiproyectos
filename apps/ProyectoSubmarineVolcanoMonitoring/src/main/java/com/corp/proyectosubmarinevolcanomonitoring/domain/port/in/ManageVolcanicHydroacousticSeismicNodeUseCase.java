package com.corp.proyectosubmarinevolcanomonitoring.domain.port.in;

import com.corp.proyectosubmarinevolcanomonitoring.domain.model.VolcanicHydroacousticSeismicNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageVolcanicHydroacousticSeismicNodeUseCase {
    VolcanicHydroacousticSeismicNode createVolcanicHydroacousticSeismicNode(String tenantId, String title, double value);
    Optional<VolcanicHydroacousticSeismicNode> findVolcanicHydroacousticSeismicNodeById(String id, String tenantId);
    VolcanicHydroacousticSeismicNode processOptimization(String id, String tenantId);
}
