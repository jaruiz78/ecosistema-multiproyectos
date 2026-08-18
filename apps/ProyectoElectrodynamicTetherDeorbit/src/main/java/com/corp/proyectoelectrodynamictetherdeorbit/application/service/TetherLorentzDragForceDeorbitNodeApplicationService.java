package com.corp.proyectoelectrodynamictetherdeorbit.application.service;

import com.corp.proyectoelectrodynamictetherdeorbit.domain.model.TetherLorentzDragForceDeorbitNode;
import com.corp.proyectoelectrodynamictetherdeorbit.domain.port.in.ManageTetherLorentzDragForceDeorbitNodeUseCase;
import com.corp.proyectoelectrodynamictetherdeorbit.domain.port.out.TetherLorentzDragForceDeorbitNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TetherLorentzDragForceDeorbitNode.
 */
@Service
public class TetherLorentzDragForceDeorbitNodeApplicationService implements ManageTetherLorentzDragForceDeorbitNodeUseCase {

    private final TetherLorentzDragForceDeorbitNodeRepositoryPort repositoryPort;

    public TetherLorentzDragForceDeorbitNodeApplicationService(TetherLorentzDragForceDeorbitNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TetherLorentzDragForceDeorbitNode createTetherLorentzDragForceDeorbitNode(String tenantId, String title, double value) {
        TetherLorentzDragForceDeorbitNode entity = new TetherLorentzDragForceDeorbitNode(
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
    public Optional<TetherLorentzDragForceDeorbitNode> findTetherLorentzDragForceDeorbitNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TetherLorentzDragForceDeorbitNode processOptimization(String id, String tenantId) {
        TetherLorentzDragForceDeorbitNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TetherLorentzDragForceDeorbitNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
