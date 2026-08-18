package com.corp.proyectoflatopticsmetalensimaging.application.service;

import com.corp.proyectoflatopticsmetalensimaging.domain.model.MetalensPhaseProfileMatrixBatch;
import com.corp.proyectoflatopticsmetalensimaging.domain.port.in.ManageMetalensPhaseProfileMatrixBatchUseCase;
import com.corp.proyectoflatopticsmetalensimaging.domain.port.out.MetalensPhaseProfileMatrixBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MetalensPhaseProfileMatrixBatch.
 */
@Service
public class MetalensPhaseProfileMatrixBatchApplicationService implements ManageMetalensPhaseProfileMatrixBatchUseCase {

    private final MetalensPhaseProfileMatrixBatchRepositoryPort repositoryPort;

    public MetalensPhaseProfileMatrixBatchApplicationService(MetalensPhaseProfileMatrixBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MetalensPhaseProfileMatrixBatch createMetalensPhaseProfileMatrixBatch(String tenantId, String title, double value) {
        MetalensPhaseProfileMatrixBatch entity = new MetalensPhaseProfileMatrixBatch(
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
    public Optional<MetalensPhaseProfileMatrixBatch> findMetalensPhaseProfileMatrixBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MetalensPhaseProfileMatrixBatch processOptimization(String id, String tenantId) {
        MetalensPhaseProfileMatrixBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MetalensPhaseProfileMatrixBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
