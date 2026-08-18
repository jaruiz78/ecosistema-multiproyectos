package com.corp.proyectoautonomousmaritimefreighter.application.service;

import com.corp.proyectoautonomousmaritimefreighter.domain.model.MassVesselCollisionAvoidanceNode;
import com.corp.proyectoautonomousmaritimefreighter.domain.port.in.ManageMassVesselCollisionAvoidanceNodeUseCase;
import com.corp.proyectoautonomousmaritimefreighter.domain.port.out.MassVesselCollisionAvoidanceNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MassVesselCollisionAvoidanceNode.
 */
@Service
public class MassVesselCollisionAvoidanceNodeApplicationService implements ManageMassVesselCollisionAvoidanceNodeUseCase {

    private final MassVesselCollisionAvoidanceNodeRepositoryPort repositoryPort;

    public MassVesselCollisionAvoidanceNodeApplicationService(MassVesselCollisionAvoidanceNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MassVesselCollisionAvoidanceNode createMassVesselCollisionAvoidanceNode(String tenantId, String title, double value) {
        MassVesselCollisionAvoidanceNode entity = new MassVesselCollisionAvoidanceNode(
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
    public Optional<MassVesselCollisionAvoidanceNode> findMassVesselCollisionAvoidanceNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MassVesselCollisionAvoidanceNode processOptimization(String id, String tenantId) {
        MassVesselCollisionAvoidanceNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MassVesselCollisionAvoidanceNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
