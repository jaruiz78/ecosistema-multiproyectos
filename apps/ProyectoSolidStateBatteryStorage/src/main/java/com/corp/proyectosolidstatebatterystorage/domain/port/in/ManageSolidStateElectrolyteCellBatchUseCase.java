package com.corp.proyectosolidstatebatterystorage.domain.port.in;

import com.corp.proyectosolidstatebatterystorage.domain.model.SolidStateElectrolyteCellBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSolidStateElectrolyteCellBatchUseCase {
    SolidStateElectrolyteCellBatch createSolidStateElectrolyteCellBatch(String tenantId, String title, double value);
    Optional<SolidStateElectrolyteCellBatch> findSolidStateElectrolyteCellBatchById(String id, String tenantId);
    SolidStateElectrolyteCellBatch processOptimization(String id, String tenantId);
}
