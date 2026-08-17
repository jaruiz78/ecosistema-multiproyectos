package com.corp.proyectodroneairspaceuspace.application.service;

import com.corp.proyectodroneairspaceuspace.domain.model.DroneAirspaceUSpace;
import com.corp.proyectodroneairspaceuspace.domain.port.in.ManageDroneAirspaceUSpaceUseCase;
import com.corp.proyectodroneairspaceuspace.domain.port.out.DroneAirspaceUSpaceRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class DroneAirspaceUSpaceApplicationService implements ManageDroneAirspaceUSpaceUseCase {

    private final DroneAirspaceUSpaceRepositoryPort repositoryPort;

    public DroneAirspaceUSpaceApplicationService(DroneAirspaceUSpaceRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DroneAirspaceUSpace createDroneAirspaceUSpace(String tenantId, String title, double value) {
        DroneAirspaceUSpace entity = new DroneAirspaceUSpace(
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
    public Optional<DroneAirspaceUSpace> findDroneAirspaceUSpaceById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DroneAirspaceUSpace processOptimization(String id, String tenantId) {
        DroneAirspaceUSpace existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DroneAirspaceUSpace optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
