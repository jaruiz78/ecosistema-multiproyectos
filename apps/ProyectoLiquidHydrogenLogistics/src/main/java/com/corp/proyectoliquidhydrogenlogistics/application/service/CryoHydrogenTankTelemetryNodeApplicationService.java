package com.corp.proyectoliquidhydrogenlogistics.application.service;

import com.corp.proyectoliquidhydrogenlogistics.domain.model.CryoHydrogenTankTelemetryNode;
import com.corp.proyectoliquidhydrogenlogistics.domain.port.in.ManageCryoHydrogenTankTelemetryNodeUseCase;
import com.corp.proyectoliquidhydrogenlogistics.domain.port.out.CryoHydrogenTankTelemetryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CryoHydrogenTankTelemetryNode.
 */
@Service
public class CryoHydrogenTankTelemetryNodeApplicationService implements ManageCryoHydrogenTankTelemetryNodeUseCase {

    private final CryoHydrogenTankTelemetryNodeRepositoryPort repositoryPort;

    public CryoHydrogenTankTelemetryNodeApplicationService(CryoHydrogenTankTelemetryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CryoHydrogenTankTelemetryNode createCryoHydrogenTankTelemetryNode(String tenantId, String title, double value) {
        CryoHydrogenTankTelemetryNode entity = new CryoHydrogenTankTelemetryNode(
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
    public Optional<CryoHydrogenTankTelemetryNode> findCryoHydrogenTankTelemetryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CryoHydrogenTankTelemetryNode processOptimization(String id, String tenantId) {
        CryoHydrogenTankTelemetryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CryoHydrogenTankTelemetryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
