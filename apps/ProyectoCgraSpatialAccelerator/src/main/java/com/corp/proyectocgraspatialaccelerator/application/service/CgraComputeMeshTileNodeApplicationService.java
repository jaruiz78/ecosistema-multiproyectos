package com.corp.proyectocgraspatialaccelerator.application.service;

import com.corp.proyectocgraspatialaccelerator.domain.model.CgraComputeMeshTileNode;
import com.corp.proyectocgraspatialaccelerator.domain.port.in.ManageCgraComputeMeshTileNodeUseCase;
import com.corp.proyectocgraspatialaccelerator.domain.port.out.CgraComputeMeshTileNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CgraComputeMeshTileNode.
 */
@Service
public class CgraComputeMeshTileNodeApplicationService implements ManageCgraComputeMeshTileNodeUseCase {

    private final CgraComputeMeshTileNodeRepositoryPort repositoryPort;

    public CgraComputeMeshTileNodeApplicationService(CgraComputeMeshTileNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CgraComputeMeshTileNode createCgraComputeMeshTileNode(String tenantId, String title, double value) {
        CgraComputeMeshTileNode entity = new CgraComputeMeshTileNode(
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
    public Optional<CgraComputeMeshTileNode> findCgraComputeMeshTileNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CgraComputeMeshTileNode processOptimization(String id, String tenantId) {
        CgraComputeMeshTileNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CgraComputeMeshTileNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
