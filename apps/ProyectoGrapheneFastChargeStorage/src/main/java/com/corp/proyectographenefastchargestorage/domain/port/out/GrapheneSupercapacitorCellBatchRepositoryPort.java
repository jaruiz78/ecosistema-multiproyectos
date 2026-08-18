package com.corp.proyectographenefastchargestorage.domain.port.out;

import com.corp.proyectographenefastchargestorage.domain.model.GrapheneSupercapacitorCellBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GrapheneSupercapacitorCellBatchRepositoryPort {
    GrapheneSupercapacitorCellBatch save(GrapheneSupercapacitorCellBatch entity);
    Optional<GrapheneSupercapacitorCellBatch> findById(String id, String tenantId);
}
