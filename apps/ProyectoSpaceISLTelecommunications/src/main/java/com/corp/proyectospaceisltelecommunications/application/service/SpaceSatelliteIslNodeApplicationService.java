package com.corp.proyectospaceisltelecommunications.application.service;

import com.corp.proyectospaceisltelecommunications.domain.model.SpaceSatelliteIslNode;
import com.corp.proyectospaceisltelecommunications.domain.port.in.ManageSpaceSatelliteIslNodeUseCase;
import com.corp.proyectospaceisltelecommunications.domain.port.out.SpaceSatelliteIslNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpaceSatelliteIslNode.
 */
@Service
public class SpaceSatelliteIslNodeApplicationService implements ManageSpaceSatelliteIslNodeUseCase {

    private final SpaceSatelliteIslNodeRepositoryPort repositoryPort;

    public SpaceSatelliteIslNodeApplicationService(SpaceSatelliteIslNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpaceSatelliteIslNode createSpaceSatelliteIslNode(String tenantId, String title, double value) {
        SpaceSatelliteIslNode entity = new SpaceSatelliteIslNode(
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
    public Optional<SpaceSatelliteIslNode> findSpaceSatelliteIslNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpaceSatelliteIslNode processOptimization(String id, String tenantId) {
        SpaceSatelliteIslNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpaceSatelliteIslNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
