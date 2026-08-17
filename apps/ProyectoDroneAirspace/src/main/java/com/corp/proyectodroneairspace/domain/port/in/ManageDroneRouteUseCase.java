package com.corp.proyectodroneairspace.domain.port.in;

import com.corp.proyectodroneairspace.domain.model.DroneRoute;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDroneRouteUseCase {
    DroneRoute createDroneRoute(String tenantId, String title, double value);
    Optional<DroneRoute> findDroneRouteById(String id, String tenantId);
    DroneRoute processOptimization(String id, String tenantId);
}
