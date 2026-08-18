package com.corp.proyectohighpowersolarelectrictug.application.service;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import com.corp.proyectohighpowersolarelectrictug.domain.port.in.ManageSepXenonMassFlowThrustNodeUseCase;
import com.corp.proyectohighpowersolarelectrictug.domain.port.out.SepXenonMassFlowThrustNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SepXenonMassFlowThrustNode.
 */
@Service
public class SepXenonMassFlowThrustNodeApplicationService implements ManageSepXenonMassFlowThrustNodeUseCase {

    private final SepXenonMassFlowThrustNodeRepositoryPort repositoryPort;

    public SepXenonMassFlowThrustNodeApplicationService(SepXenonMassFlowThrustNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SepXenonMassFlowThrustNode createSepXenonMassFlowThrustNode(String tenantId, String title, double value) {
        SepXenonMassFlowThrustNode entity = new SepXenonMassFlowThrustNode(
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
    public Optional<SepXenonMassFlowThrustNode> findSepXenonMassFlowThrustNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SepXenonMassFlowThrustNode processOptimization(String id, String tenantId) {
        SepXenonMassFlowThrustNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SepXenonMassFlowThrustNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
