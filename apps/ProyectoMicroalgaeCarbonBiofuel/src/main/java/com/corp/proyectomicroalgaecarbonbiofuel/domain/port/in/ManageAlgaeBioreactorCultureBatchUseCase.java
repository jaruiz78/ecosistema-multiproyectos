package com.corp.proyectomicroalgaecarbonbiofuel.domain.port.in;

import com.corp.proyectomicroalgaecarbonbiofuel.domain.model.AlgaeBioreactorCultureBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAlgaeBioreactorCultureBatchUseCase {
    AlgaeBioreactorCultureBatch createAlgaeBioreactorCultureBatch(String tenantId, String title, double value);
    Optional<AlgaeBioreactorCultureBatch> findAlgaeBioreactorCultureBatchById(String id, String tenantId);
    AlgaeBioreactorCultureBatch processOptimization(String id, String tenantId);
}
