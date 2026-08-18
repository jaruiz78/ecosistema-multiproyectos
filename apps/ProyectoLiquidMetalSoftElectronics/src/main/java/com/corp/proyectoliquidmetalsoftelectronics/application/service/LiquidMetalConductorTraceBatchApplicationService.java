package com.corp.proyectoliquidmetalsoftelectronics.application.service;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.in.ManageLiquidMetalConductorTraceBatchUseCase;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.out.LiquidMetalConductorTraceBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LiquidMetalConductorTraceBatch.
 */
@Service
public class LiquidMetalConductorTraceBatchApplicationService implements ManageLiquidMetalConductorTraceBatchUseCase {

    private final LiquidMetalConductorTraceBatchRepositoryPort repositoryPort;

    public LiquidMetalConductorTraceBatchApplicationService(LiquidMetalConductorTraceBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LiquidMetalConductorTraceBatch createLiquidMetalConductorTraceBatch(String tenantId, String title, double value) {
        LiquidMetalConductorTraceBatch entity = new LiquidMetalConductorTraceBatch(
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
    public Optional<LiquidMetalConductorTraceBatch> findLiquidMetalConductorTraceBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LiquidMetalConductorTraceBatch processOptimization(String id, String tenantId) {
        LiquidMetalConductorTraceBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LiquidMetalConductorTraceBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
