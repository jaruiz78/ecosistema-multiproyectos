package com.corp.proyectodeepgeothermalenergytwin.application.service;

import com.corp.proyectodeepgeothermalenergytwin.domain.model.GeothermalBoreholeHeatExchangerNode;
import com.corp.proyectodeepgeothermalenergytwin.domain.port.in.ManageGeothermalBoreholeHeatExchangerNodeUseCase;
import com.corp.proyectodeepgeothermalenergytwin.domain.port.out.GeothermalBoreholeHeatExchangerNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GeothermalBoreholeHeatExchangerNode.
 */
@Service
public class GeothermalBoreholeHeatExchangerNodeApplicationService implements ManageGeothermalBoreholeHeatExchangerNodeUseCase {

    private final GeothermalBoreholeHeatExchangerNodeRepositoryPort repositoryPort;

    public GeothermalBoreholeHeatExchangerNodeApplicationService(GeothermalBoreholeHeatExchangerNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GeothermalBoreholeHeatExchangerNode createGeothermalBoreholeHeatExchangerNode(String tenantId, String title, double value) {
        GeothermalBoreholeHeatExchangerNode entity = new GeothermalBoreholeHeatExchangerNode(
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
    public Optional<GeothermalBoreholeHeatExchangerNode> findGeothermalBoreholeHeatExchangerNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GeothermalBoreholeHeatExchangerNode processOptimization(String id, String tenantId) {
        GeothermalBoreholeHeatExchangerNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GeothermalBoreholeHeatExchangerNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
