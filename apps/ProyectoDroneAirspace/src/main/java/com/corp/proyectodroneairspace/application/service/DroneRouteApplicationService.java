package com.corp.proyectodroneairspace.application.service;

import com.corp.proyectodroneairspace.domain.model.DroneRoute;
import com.corp.proyectodroneairspace.domain.port.in.ManageDroneRouteUseCase;
import com.corp.proyectodroneairspace.domain.port.out.DroneRouteRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DroneRoute.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DroneRouteApplicationService implements ManageDroneRouteUseCase {

    private final DroneRouteRepositoryPort repositoryPort;

    public DroneRouteApplicationService(DroneRouteRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DroneRoute createDroneRoute(String tenantId, String title, double value) {
        DroneRoute entity = new DroneRoute(
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
    public Optional<DroneRoute> findDroneRouteById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DroneRoute processOptimization(String id, String tenantId) {
        DroneRoute existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DroneRoute optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
