package com.corp.proyectocryoagrifoodlogistics.domain.port.in;

import com.corp.proyectocryoagrifoodlogistics.domain.model.CryogenicTelemetryBatchNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCryogenicTelemetryBatchNodeUseCase {
    CryogenicTelemetryBatchNode createCryogenicTelemetryBatchNode(String tenantId, String title, double value);
    Optional<CryogenicTelemetryBatchNode> findCryogenicTelemetryBatchNodeById(String id, String tenantId);
    CryogenicTelemetryBatchNode processOptimization(String id, String tenantId);
}
