package com.corp.proyectohyperloopintercitycorridor.domain.port.out;

import com.corp.proyectohyperloopintercitycorridor.domain.model.HyperloopPodTelemetryTelemetryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HyperloopPodTelemetryTelemetryNodeRepositoryPort {
    HyperloopPodTelemetryTelemetryNode save(HyperloopPodTelemetryTelemetryNode entity);
    Optional<HyperloopPodTelemetryTelemetryNode> findById(String id, String tenantId);
}
