package com.corp.proyectosolidstatebatterystorage.domain.port.out;

import com.corp.proyectosolidstatebatterystorage.domain.model.SolidStateElectrolyteCellBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SolidStateElectrolyteCellBatchRepositoryPort {
    SolidStateElectrolyteCellBatch save(SolidStateElectrolyteCellBatch entity);
    Optional<SolidStateElectrolyteCellBatch> findById(String id, String tenantId);
}
