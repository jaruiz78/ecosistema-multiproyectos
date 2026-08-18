package com.corp.proyectohypersonicintercontinentalfreight.domain.port.out;

import com.corp.proyectohypersonicintercontinentalfreight.domain.model.ScramjetCombustionPressureRatioNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ScramjetCombustionPressureRatioNodeRepositoryPort {
    ScramjetCombustionPressureRatioNode save(ScramjetCombustionPressureRatioNode entity);
    Optional<ScramjetCombustionPressureRatioNode> findById(String id, String tenantId);
}
