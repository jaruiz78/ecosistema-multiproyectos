package com.corp.proyectodroneairspace.domain.port.out;

import com.corp.proyectodroneairspace.domain.model.DroneRoute;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DroneRouteRepositoryPort {
    DroneRoute save(DroneRoute entity);
    Optional<DroneRoute> findById(String id, String tenantId);
}
