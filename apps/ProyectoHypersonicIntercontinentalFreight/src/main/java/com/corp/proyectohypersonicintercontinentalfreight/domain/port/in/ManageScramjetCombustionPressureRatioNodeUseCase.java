package com.corp.proyectohypersonicintercontinentalfreight.domain.port.in;

import com.corp.proyectohypersonicintercontinentalfreight.domain.model.ScramjetCombustionPressureRatioNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageScramjetCombustionPressureRatioNodeUseCase {
    ScramjetCombustionPressureRatioNode createScramjetCombustionPressureRatioNode(String tenantId, String title, double value);
    Optional<ScramjetCombustionPressureRatioNode> findScramjetCombustionPressureRatioNodeById(String id, String tenantId);
    ScramjetCombustionPressureRatioNode processOptimization(String id, String tenantId);
}
