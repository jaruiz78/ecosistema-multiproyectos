package com.corp.proyectoquantumdottandemsolar.domain.port.in;

import com.corp.proyectoquantumdottandemsolar.domain.model.QuantumDotSolarCellBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageQuantumDotSolarCellBatchUseCase {
    QuantumDotSolarCellBatch createQuantumDotSolarCellBatch(String tenantId, String title, double value);
    Optional<QuantumDotSolarCellBatch> findQuantumDotSolarCellBatchById(String id, String tenantId);
    QuantumDotSolarCellBatch processOptimization(String id, String tenantId);
}
