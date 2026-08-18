package com.corp.proyectoflatopticsmetalensimaging.domain.port.in;

import com.corp.proyectoflatopticsmetalensimaging.domain.model.MetalensPhaseProfileMatrixBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMetalensPhaseProfileMatrixBatchUseCase {
    MetalensPhaseProfileMatrixBatch createMetalensPhaseProfileMatrixBatch(String tenantId, String title, double value);
    Optional<MetalensPhaseProfileMatrixBatch> findMetalensPhaseProfileMatrixBatchById(String id, String tenantId);
    MetalensPhaseProfileMatrixBatch processOptimization(String id, String tenantId);
}
