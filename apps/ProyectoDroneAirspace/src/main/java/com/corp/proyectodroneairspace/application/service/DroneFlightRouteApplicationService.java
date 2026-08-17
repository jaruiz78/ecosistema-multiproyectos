package com.corp.proyectodroneairspace.application.service;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import com.corp.proyectodroneairspace.domain.port.in.ManageDroneFlightRouteUseCase;
import com.corp.proyectodroneairspace.domain.port.out.DroneFlightRouteRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DroneFlightRoute.
 */
@Service
public class DroneFlightRouteApplicationService implements ManageDroneFlightRouteUseCase {

    private final DroneFlightRouteRepositoryPort repositoryPort;

    public DroneFlightRouteApplicationService(DroneFlightRouteRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DroneFlightRoute createDroneFlightRoute(String tenantId, String title, double value) {
        DroneFlightRoute entity = new DroneFlightRoute(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<DroneFlightRoute> findDroneFlightRouteById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DroneFlightRoute processOptimization(String id, String tenantId) {
        DroneFlightRoute existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DroneFlightRoute optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
