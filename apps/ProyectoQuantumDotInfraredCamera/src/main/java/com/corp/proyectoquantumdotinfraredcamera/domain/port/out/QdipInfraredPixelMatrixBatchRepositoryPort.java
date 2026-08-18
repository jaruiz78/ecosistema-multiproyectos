package com.corp.proyectoquantumdotinfraredcamera.domain.port.out;

import com.corp.proyectoquantumdotinfraredcamera.domain.model.QdipInfraredPixelMatrixBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QdipInfraredPixelMatrixBatchRepositoryPort {
    QdipInfraredPixelMatrixBatch save(QdipInfraredPixelMatrixBatch entity);
    Optional<QdipInfraredPixelMatrixBatch> findById(String id, String tenantId);
}
