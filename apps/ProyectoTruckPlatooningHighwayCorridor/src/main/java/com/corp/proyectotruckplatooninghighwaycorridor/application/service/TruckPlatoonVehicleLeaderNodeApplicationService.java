package com.corp.proyectotruckplatooninghighwaycorridor.application.service;

import com.corp.proyectotruckplatooninghighwaycorridor.domain.model.TruckPlatoonVehicleLeaderNode;
import com.corp.proyectotruckplatooninghighwaycorridor.domain.port.in.ManageTruckPlatoonVehicleLeaderNodeUseCase;
import com.corp.proyectotruckplatooninghighwaycorridor.domain.port.out.TruckPlatoonVehicleLeaderNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TruckPlatoonVehicleLeaderNode.
 */
@Service
public class TruckPlatoonVehicleLeaderNodeApplicationService implements ManageTruckPlatoonVehicleLeaderNodeUseCase {

    private final TruckPlatoonVehicleLeaderNodeRepositoryPort repositoryPort;

    public TruckPlatoonVehicleLeaderNodeApplicationService(TruckPlatoonVehicleLeaderNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TruckPlatoonVehicleLeaderNode createTruckPlatoonVehicleLeaderNode(String tenantId, String title, double value) {
        TruckPlatoonVehicleLeaderNode entity = new TruckPlatoonVehicleLeaderNode(
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
    public Optional<TruckPlatoonVehicleLeaderNode> findTruckPlatoonVehicleLeaderNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TruckPlatoonVehicleLeaderNode processOptimization(String id, String tenantId) {
        TruckPlatoonVehicleLeaderNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TruckPlatoonVehicleLeaderNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
