package com.corp.proyectomaritimeautonomousfleet.application.service;

import com.corp.proyectomaritimeautonomousfleet.domain.model.AutonomousVesselVoyage;
import com.corp.proyectomaritimeautonomousfleet.domain.port.in.ManageAutonomousVesselVoyageUseCase;
import com.corp.proyectomaritimeautonomousfleet.domain.port.out.AutonomousVesselVoyageRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AutonomousVesselVoyage.
 */
@Service
public class AutonomousVesselVoyageApplicationService implements ManageAutonomousVesselVoyageUseCase {

    private final AutonomousVesselVoyageRepositoryPort repositoryPort;

    public AutonomousVesselVoyageApplicationService(AutonomousVesselVoyageRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AutonomousVesselVoyage createAutonomousVesselVoyage(String tenantId, String title, double value) {
        AutonomousVesselVoyage entity = new AutonomousVesselVoyage(
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
    public Optional<AutonomousVesselVoyage> findAutonomousVesselVoyageById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AutonomousVesselVoyage processOptimization(String id, String tenantId) {
        AutonomousVesselVoyage existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AutonomousVesselVoyage optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
