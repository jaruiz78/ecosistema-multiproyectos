package com.corp.proyectohyperloopintercitycorridor.application.service;

import com.corp.proyectohyperloopintercitycorridor.domain.model.HyperloopPodTelemetryTelemetryNode;
import com.corp.proyectohyperloopintercitycorridor.domain.port.in.ManageHyperloopPodTelemetryTelemetryNodeUseCase;
import com.corp.proyectohyperloopintercitycorridor.domain.port.out.HyperloopPodTelemetryTelemetryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HyperloopPodTelemetryTelemetryNode.
 */
@Service
public class HyperloopPodTelemetryTelemetryNodeApplicationService implements ManageHyperloopPodTelemetryTelemetryNodeUseCase {

    private final HyperloopPodTelemetryTelemetryNodeRepositoryPort repositoryPort;

    public HyperloopPodTelemetryTelemetryNodeApplicationService(HyperloopPodTelemetryTelemetryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HyperloopPodTelemetryTelemetryNode createHyperloopPodTelemetryTelemetryNode(String tenantId, String title, double value) {
        HyperloopPodTelemetryTelemetryNode entity = new HyperloopPodTelemetryTelemetryNode(
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
    public Optional<HyperloopPodTelemetryTelemetryNode> findHyperloopPodTelemetryTelemetryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HyperloopPodTelemetryTelemetryNode processOptimization(String id, String tenantId) {
        HyperloopPodTelemetryTelemetryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HyperloopPodTelemetryTelemetryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
