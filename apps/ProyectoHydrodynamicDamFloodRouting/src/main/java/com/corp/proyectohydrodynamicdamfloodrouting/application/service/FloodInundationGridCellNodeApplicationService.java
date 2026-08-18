package com.corp.proyectohydrodynamicdamfloodrouting.application.service;

import com.corp.proyectohydrodynamicdamfloodrouting.domain.model.FloodInundationGridCellNode;
import com.corp.proyectohydrodynamicdamfloodrouting.domain.port.in.ManageFloodInundationGridCellNodeUseCase;
import com.corp.proyectohydrodynamicdamfloodrouting.domain.port.out.FloodInundationGridCellNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de FloodInundationGridCellNode.
 */
@Service
public class FloodInundationGridCellNodeApplicationService implements ManageFloodInundationGridCellNodeUseCase {

    private final FloodInundationGridCellNodeRepositoryPort repositoryPort;

    public FloodInundationGridCellNodeApplicationService(FloodInundationGridCellNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public FloodInundationGridCellNode createFloodInundationGridCellNode(String tenantId, String title, double value) {
        FloodInundationGridCellNode entity = new FloodInundationGridCellNode(
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
    public Optional<FloodInundationGridCellNode> findFloodInundationGridCellNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public FloodInundationGridCellNode processOptimization(String id, String tenantId) {
        FloodInundationGridCellNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        FloodInundationGridCellNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
