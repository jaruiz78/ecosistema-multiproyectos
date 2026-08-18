package com.corp.proyectourbanenergymobilitynexus.application.service;

import com.corp.proyectourbanenergymobilitynexus.domain.model.V2GChargingDispatchNode;
import com.corp.proyectourbanenergymobilitynexus.domain.port.in.ManageV2GChargingDispatchNodeUseCase;
import com.corp.proyectourbanenergymobilitynexus.domain.port.out.V2GChargingDispatchNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de V2GChargingDispatchNode.
 */
@Service
public class V2GChargingDispatchNodeApplicationService implements ManageV2GChargingDispatchNodeUseCase {

    private final V2GChargingDispatchNodeRepositoryPort repositoryPort;

    public V2GChargingDispatchNodeApplicationService(V2GChargingDispatchNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public V2GChargingDispatchNode createV2GChargingDispatchNode(String tenantId, String title, double value) {
        V2GChargingDispatchNode entity = new V2GChargingDispatchNode(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<V2GChargingDispatchNode> findV2GChargingDispatchNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public V2GChargingDispatchNode processOptimization(String id, String tenantId) {
        V2GChargingDispatchNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        V2GChargingDispatchNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
