package com.corp.proyectoastroturismostarlight.domain.port.out;

import com.corp.proyectoastroturismostarlight.domain.model.StarlightObservationPoint;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface StarlightObservationPointRepositoryPort {
    StarlightObservationPoint save(StarlightObservationPoint entity);
    Optional<StarlightObservationPoint> findById(String id, String tenantId);
}
