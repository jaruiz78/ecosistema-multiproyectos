package com.corp.proyectohyperloopintercitycorridor.domain.port.in;

import com.corp.proyectohyperloopintercitycorridor.domain.model.HyperloopPodTelemetryTelemetryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHyperloopPodTelemetryTelemetryNodeUseCase {
    HyperloopPodTelemetryTelemetryNode createHyperloopPodTelemetryTelemetryNode(String tenantId, String title, double value);
    Optional<HyperloopPodTelemetryTelemetryNode> findHyperloopPodTelemetryTelemetryNodeById(String id, String tenantId);
    HyperloopPodTelemetryTelemetryNode processOptimization(String id, String tenantId);
}
