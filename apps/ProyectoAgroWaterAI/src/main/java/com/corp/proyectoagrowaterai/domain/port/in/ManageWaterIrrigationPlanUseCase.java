package com.corp.proyectoagrowaterai.domain.port.in;

import com.corp.proyectoagrowaterai.domain.model.WaterIrrigationPlan;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageWaterIrrigationPlanUseCase {
    WaterIrrigationPlan createWaterIrrigationPlan(String tenantId, String title, double value);
    Optional<WaterIrrigationPlan> findWaterIrrigationPlanById(String id, String tenantId);
    WaterIrrigationPlan processOptimization(String id, String tenantId);
}
