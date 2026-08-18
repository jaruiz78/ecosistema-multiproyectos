package com.corp.proyectoquantumdottandemsolar.domain.port.out;

import com.corp.proyectoquantumdottandemsolar.domain.model.QuantumDotSolarCellBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface QuantumDotSolarCellBatchRepositoryPort {
    QuantumDotSolarCellBatch save(QuantumDotSolarCellBatch entity);
    Optional<QuantumDotSolarCellBatch> findById(String id, String tenantId);
}
