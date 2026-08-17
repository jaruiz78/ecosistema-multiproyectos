package com.corp.proyectodroneairspace.domain.port.out;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DroneFlightRouteRepositoryPort {
    DroneFlightRoute save(DroneFlightRoute entity);
    Optional<DroneFlightRoute> findById(String id, String tenantId);
}
