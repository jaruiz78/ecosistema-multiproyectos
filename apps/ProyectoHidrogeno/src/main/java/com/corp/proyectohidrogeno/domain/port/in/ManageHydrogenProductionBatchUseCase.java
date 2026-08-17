package com.corp.proyectohidrogeno.domain.port.in;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHydrogenProductionBatchUseCase {
    HydrogenProductionBatch createHydrogenProductionBatch(String tenantId, String title, double value);
    Optional<HydrogenProductionBatch> findHydrogenProductionBatchById(String id, String tenantId);
    HydrogenProductionBatch processOptimization(String id, String tenantId);
}
