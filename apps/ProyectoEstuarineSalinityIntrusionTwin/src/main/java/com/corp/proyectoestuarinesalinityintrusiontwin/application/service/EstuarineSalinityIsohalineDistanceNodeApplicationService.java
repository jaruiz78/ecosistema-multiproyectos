package com.corp.proyectoestuarinesalinityintrusiontwin.application.service;

import com.corp.proyectoestuarinesalinityintrusiontwin.domain.model.EstuarineSalinityIsohalineDistanceNode;
import com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.in.ManageEstuarineSalinityIsohalineDistanceNodeUseCase;
import com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.out.EstuarineSalinityIsohalineDistanceNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EstuarineSalinityIsohalineDistanceNode.
 */
@Service
public class EstuarineSalinityIsohalineDistanceNodeApplicationService implements ManageEstuarineSalinityIsohalineDistanceNodeUseCase {

    private final EstuarineSalinityIsohalineDistanceNodeRepositoryPort repositoryPort;

    public EstuarineSalinityIsohalineDistanceNodeApplicationService(EstuarineSalinityIsohalineDistanceNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EstuarineSalinityIsohalineDistanceNode createEstuarineSalinityIsohalineDistanceNode(String tenantId, String title, double value) {
        EstuarineSalinityIsohalineDistanceNode entity = new EstuarineSalinityIsohalineDistanceNode(
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
    public Optional<EstuarineSalinityIsohalineDistanceNode> findEstuarineSalinityIsohalineDistanceNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EstuarineSalinityIsohalineDistanceNode processOptimization(String id, String tenantId) {
        EstuarineSalinityIsohalineDistanceNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EstuarineSalinityIsohalineDistanceNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
