package com.corp.proyectoquantumdotinfraredcamera.domain.port.in;

import com.corp.proyectoquantumdotinfraredcamera.domain.model.QdipInfraredPixelMatrixBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQdipInfraredPixelMatrixBatchUseCase {
    QdipInfraredPixelMatrixBatch createQdipInfraredPixelMatrixBatch(String tenantId, String title, double value);
    Optional<QdipInfraredPixelMatrixBatch> findQdipInfraredPixelMatrixBatchById(String id, String tenantId);
    QdipInfraredPixelMatrixBatch processOptimization(String id, String tenantId);
}
