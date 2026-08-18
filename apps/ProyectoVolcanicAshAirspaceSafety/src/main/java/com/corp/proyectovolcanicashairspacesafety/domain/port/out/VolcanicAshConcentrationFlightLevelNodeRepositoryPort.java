package com.corp.proyectovolcanicashairspacesafety.domain.port.out;

import com.corp.proyectovolcanicashairspacesafety.domain.model.VolcanicAshConcentrationFlightLevelNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface VolcanicAshConcentrationFlightLevelNodeRepositoryPort {
    VolcanicAshConcentrationFlightLevelNode save(VolcanicAshConcentrationFlightLevelNode entity);
    Optional<VolcanicAshConcentrationFlightLevelNode> findById(String id, String tenantId);
}
