package com.corp.proyectomicroalgaecarbonbiofuel.domain.port.out;

import com.corp.proyectomicroalgaecarbonbiofuel.domain.model.AlgaeBioreactorCultureBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AlgaeBioreactorCultureBatchRepositoryPort {
    AlgaeBioreactorCultureBatch save(AlgaeBioreactorCultureBatch entity);
    Optional<AlgaeBioreactorCultureBatch> findById(String id, String tenantId);
}
