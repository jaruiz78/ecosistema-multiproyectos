package com.corp.proyectosubmarinevolcanomonitoring.domain.port.out;

import com.corp.proyectosubmarinevolcanomonitoring.domain.model.VolcanicHydroacousticSeismicNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface VolcanicHydroacousticSeismicNodeRepositoryPort {
    VolcanicHydroacousticSeismicNode save(VolcanicHydroacousticSeismicNode entity);
    Optional<VolcanicHydroacousticSeismicNode> findById(String id, String tenantId);
}
