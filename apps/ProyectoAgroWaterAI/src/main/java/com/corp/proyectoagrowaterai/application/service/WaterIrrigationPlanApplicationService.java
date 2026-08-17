package com.corp.proyectoagrowaterai.application.service;

import com.corp.proyectoagrowaterai.domain.model.WaterIrrigationPlan;
import com.corp.proyectoagrowaterai.domain.port.in.ManageWaterIrrigationPlanUseCase;
import com.corp.proyectoagrowaterai.domain.port.out.WaterIrrigationPlanRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de WaterIrrigationPlan.
 */
@Service
public class WaterIrrigationPlanApplicationService implements ManageWaterIrrigationPlanUseCase {

    private final WaterIrrigationPlanRepositoryPort repositoryPort;

    public WaterIrrigationPlanApplicationService(WaterIrrigationPlanRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public WaterIrrigationPlan createWaterIrrigationPlan(String tenantId, String title, double value) {
        WaterIrrigationPlan entity = new WaterIrrigationPlan(
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
    public Optional<WaterIrrigationPlan> findWaterIrrigationPlanById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public WaterIrrigationPlan processOptimization(String id, String tenantId) {
        WaterIrrigationPlan existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        WaterIrrigationPlan optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
