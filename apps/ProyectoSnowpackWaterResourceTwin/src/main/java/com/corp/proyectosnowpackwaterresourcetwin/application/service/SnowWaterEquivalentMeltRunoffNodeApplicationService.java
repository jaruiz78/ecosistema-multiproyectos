package com.corp.proyectosnowpackwaterresourcetwin.application.service;

import com.corp.proyectosnowpackwaterresourcetwin.domain.model.SnowWaterEquivalentMeltRunoffNode;
import com.corp.proyectosnowpackwaterresourcetwin.domain.port.in.ManageSnowWaterEquivalentMeltRunoffNodeUseCase;
import com.corp.proyectosnowpackwaterresourcetwin.domain.port.out.SnowWaterEquivalentMeltRunoffNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SnowWaterEquivalentMeltRunoffNode.
 */
@Service
public class SnowWaterEquivalentMeltRunoffNodeApplicationService implements ManageSnowWaterEquivalentMeltRunoffNodeUseCase {

    private final SnowWaterEquivalentMeltRunoffNodeRepositoryPort repositoryPort;

    public SnowWaterEquivalentMeltRunoffNodeApplicationService(SnowWaterEquivalentMeltRunoffNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SnowWaterEquivalentMeltRunoffNode createSnowWaterEquivalentMeltRunoffNode(String tenantId, String title, double value) {
        SnowWaterEquivalentMeltRunoffNode entity = new SnowWaterEquivalentMeltRunoffNode(
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
    public Optional<SnowWaterEquivalentMeltRunoffNode> findSnowWaterEquivalentMeltRunoffNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SnowWaterEquivalentMeltRunoffNode processOptimization(String id, String tenantId) {
        SnowWaterEquivalentMeltRunoffNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SnowWaterEquivalentMeltRunoffNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
