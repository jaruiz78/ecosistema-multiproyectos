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
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
