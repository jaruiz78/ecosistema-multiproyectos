package com.corp.proyectoagrifoodcoldchaintrace.domain.port.out;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ColdChainShipmentBatchRepositoryPort {
    ColdChainShipmentBatch save(ColdChainShipmentBatch entity);
    Optional<ColdChainShipmentBatch> findById(String id, String tenantId);
}
