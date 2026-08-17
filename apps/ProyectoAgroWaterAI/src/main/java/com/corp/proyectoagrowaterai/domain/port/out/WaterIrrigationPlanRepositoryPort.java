package com.corp.proyectoagrowaterai.domain.port.out;

import com.corp.proyectoagrowaterai.domain.model.WaterIrrigationPlan;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface WaterIrrigationPlanRepositoryPort {
    WaterIrrigationPlan save(WaterIrrigationPlan entity);
    Optional<WaterIrrigationPlan> findById(String id, String tenantId);
}
