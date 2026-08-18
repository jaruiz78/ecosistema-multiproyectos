package com.corp.proyectooceanacidificationpreserve.application.service;

import com.corp.proyectooceanacidificationpreserve.domain.model.AragoniteSaturationStateOmegaNode;
import com.corp.proyectooceanacidificationpreserve.domain.port.in.ManageAragoniteSaturationStateOmegaNodeUseCase;
import com.corp.proyectooceanacidificationpreserve.domain.port.out.AragoniteSaturationStateOmegaNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AragoniteSaturationStateOmegaNode.
 */
@Service
public class AragoniteSaturationStateOmegaNodeApplicationService implements ManageAragoniteSaturationStateOmegaNodeUseCase {

    private final AragoniteSaturationStateOmegaNodeRepositoryPort repositoryPort;

    public AragoniteSaturationStateOmegaNodeApplicationService(AragoniteSaturationStateOmegaNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AragoniteSaturationStateOmegaNode createAragoniteSaturationStateOmegaNode(String tenantId, String title, double value) {
        AragoniteSaturationStateOmegaNode entity = new AragoniteSaturationStateOmegaNode(
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
    public Optional<AragoniteSaturationStateOmegaNode> findAragoniteSaturationStateOmegaNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AragoniteSaturationStateOmegaNode processOptimization(String id, String tenantId) {
        AragoniteSaturationStateOmegaNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AragoniteSaturationStateOmegaNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
