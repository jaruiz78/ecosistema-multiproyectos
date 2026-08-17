package com.corp.proyectohidrogeno.domain.port.out;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HydrogenProductionBatchRepositoryPort {
    HydrogenProductionBatch save(HydrogenProductionBatch entity);
    Optional<HydrogenProductionBatch> findById(String id, String tenantId);
}
