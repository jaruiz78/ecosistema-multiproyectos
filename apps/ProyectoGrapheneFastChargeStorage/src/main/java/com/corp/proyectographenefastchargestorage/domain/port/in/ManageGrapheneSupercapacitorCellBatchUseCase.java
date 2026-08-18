package com.corp.proyectographenefastchargestorage.domain.port.in;

import com.corp.proyectographenefastchargestorage.domain.model.GrapheneSupercapacitorCellBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGrapheneSupercapacitorCellBatchUseCase {
    GrapheneSupercapacitorCellBatch createGrapheneSupercapacitorCellBatch(String tenantId, String title, double value);
    Optional<GrapheneSupercapacitorCellBatch> findGrapheneSupercapacitorCellBatchById(String id, String tenantId);
    GrapheneSupercapacitorCellBatch processOptimization(String id, String tenantId);
}
