package com.corp.proyectodroneairspace.domain.port.in;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDroneFlightRouteUseCase {
    DroneFlightRoute createDroneFlightRoute(String tenantId, String title, double value);
    Optional<DroneFlightRoute> findDroneFlightRouteById(String id, String tenantId);
    DroneFlightRoute processOptimization(String id, String tenantId);
}
