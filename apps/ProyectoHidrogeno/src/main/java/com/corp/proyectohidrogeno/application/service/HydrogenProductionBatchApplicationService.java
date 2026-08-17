package com.corp.proyectohidrogeno.application.service;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import com.corp.proyectohidrogeno.domain.port.in.ManageHydrogenProductionBatchUseCase;
import com.corp.proyectohidrogeno.domain.port.out.HydrogenProductionBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HydrogenProductionBatch.
 */
@Service
public class HydrogenProductionBatchApplicationService implements ManageHydrogenProductionBatchUseCase {

    private final HydrogenProductionBatchRepositoryPort repositoryPort;

    public HydrogenProductionBatchApplicationService(HydrogenProductionBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HydrogenProductionBatch createHydrogenProductionBatch(String tenantId, String title, double value) {
        HydrogenProductionBatch entity = new HydrogenProductionBatch(
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
    public Optional<HydrogenProductionBatch> findHydrogenProductionBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HydrogenProductionBatch processOptimization(String id, String tenantId) {
        HydrogenProductionBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HydrogenProductionBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
