package com.corp.proyectoautonomouslunarroverexplorer.application.service;

import com.corp.proyectoautonomouslunarroverexplorer.domain.model.RoverWheelSlipTerramechanicsNode;
import com.corp.proyectoautonomouslunarroverexplorer.domain.port.in.ManageRoverWheelSlipTerramechanicsNodeUseCase;
import com.corp.proyectoautonomouslunarroverexplorer.domain.port.out.RoverWheelSlipTerramechanicsNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de RoverWheelSlipTerramechanicsNode.
 */
@Service
public class RoverWheelSlipTerramechanicsNodeApplicationService implements ManageRoverWheelSlipTerramechanicsNodeUseCase {

    private final RoverWheelSlipTerramechanicsNodeRepositoryPort repositoryPort;

    public RoverWheelSlipTerramechanicsNodeApplicationService(RoverWheelSlipTerramechanicsNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public RoverWheelSlipTerramechanicsNode createRoverWheelSlipTerramechanicsNode(String tenantId, String title, double value) {
        RoverWheelSlipTerramechanicsNode entity = new RoverWheelSlipTerramechanicsNode(
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
    public Optional<RoverWheelSlipTerramechanicsNode> findRoverWheelSlipTerramechanicsNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public RoverWheelSlipTerramechanicsNode processOptimization(String id, String tenantId) {
        RoverWheelSlipTerramechanicsNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        RoverWheelSlipTerramechanicsNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
