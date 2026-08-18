package com.corp.proyectoflatopticsmetalensimaging.domain.port.out;

import com.corp.proyectoflatopticsmetalensimaging.domain.model.MetalensPhaseProfileMatrixBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MetalensPhaseProfileMatrixBatchRepositoryPort {
    MetalensPhaseProfileMatrixBatch save(MetalensPhaseProfileMatrixBatch entity);
    Optional<MetalensPhaseProfileMatrixBatch> findById(String id, String tenantId);
}
