package com.corp.proyectocryoagrifoodlogistics.application.service;

import com.corp.proyectocryoagrifoodlogistics.domain.model.CryogenicTelemetryBatchNode;
import com.corp.proyectocryoagrifoodlogistics.domain.port.in.ManageCryogenicTelemetryBatchNodeUseCase;
import com.corp.proyectocryoagrifoodlogistics.domain.port.out.CryogenicTelemetryBatchNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CryogenicTelemetryBatchNode.
 */
@Service
public class CryogenicTelemetryBatchNodeApplicationService implements ManageCryogenicTelemetryBatchNodeUseCase {

    private final CryogenicTelemetryBatchNodeRepositoryPort repositoryPort;

    public CryogenicTelemetryBatchNodeApplicationService(CryogenicTelemetryBatchNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CryogenicTelemetryBatchNode createCryogenicTelemetryBatchNode(String tenantId, String title, double value) {
        CryogenicTelemetryBatchNode entity = new CryogenicTelemetryBatchNode(
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
    public Optional<CryogenicTelemetryBatchNode> findCryogenicTelemetryBatchNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CryogenicTelemetryBatchNode processOptimization(String id, String tenantId) {
        CryogenicTelemetryBatchNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CryogenicTelemetryBatchNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
