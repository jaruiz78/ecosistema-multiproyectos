package com.corp.proyectoagrifoodcoldchaintrace.domain.port.in;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageColdChainShipmentBatchUseCase {
    ColdChainShipmentBatch createColdChainShipmentBatch(String tenantId, String title, double value);
    Optional<ColdChainShipmentBatch> findColdChainShipmentBatchById(String id, String tenantId);
    ColdChainShipmentBatch processOptimization(String id, String tenantId);
}
