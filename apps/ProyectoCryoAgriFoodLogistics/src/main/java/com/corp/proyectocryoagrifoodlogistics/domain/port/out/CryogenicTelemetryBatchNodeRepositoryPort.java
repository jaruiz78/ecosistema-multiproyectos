package com.corp.proyectocryoagrifoodlogistics.domain.port.out;

import com.corp.proyectocryoagrifoodlogistics.domain.model.CryogenicTelemetryBatchNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CryogenicTelemetryBatchNodeRepositoryPort {
    CryogenicTelemetryBatchNode save(CryogenicTelemetryBatchNode entity);
    Optional<CryogenicTelemetryBatchNode> findById(String id, String tenantId);
}
