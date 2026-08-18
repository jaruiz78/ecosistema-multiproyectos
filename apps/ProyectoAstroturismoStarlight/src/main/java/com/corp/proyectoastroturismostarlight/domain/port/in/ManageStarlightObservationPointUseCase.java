package com.corp.proyectoastroturismostarlight.domain.port.in;

import com.corp.proyectoastroturismostarlight.domain.model.StarlightObservationPoint;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageStarlightObservationPointUseCase {
    StarlightObservationPoint createStarlightObservationPoint(String tenantId, String title, double value);
    Optional<StarlightObservationPoint> findStarlightObservationPointById(String id, String tenantId);
    StarlightObservationPoint processOptimization(String id, String tenantId);
}
