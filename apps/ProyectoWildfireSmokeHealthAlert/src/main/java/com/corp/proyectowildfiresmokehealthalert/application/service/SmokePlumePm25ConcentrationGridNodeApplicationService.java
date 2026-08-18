package com.corp.proyectowildfiresmokehealthalert.application.service;

import com.corp.proyectowildfiresmokehealthalert.domain.model.SmokePlumePm25ConcentrationGridNode;
import com.corp.proyectowildfiresmokehealthalert.domain.port.in.ManageSmokePlumePm25ConcentrationGridNodeUseCase;
import com.corp.proyectowildfiresmokehealthalert.domain.port.out.SmokePlumePm25ConcentrationGridNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SmokePlumePm25ConcentrationGridNode.
 */
@Service
public class SmokePlumePm25ConcentrationGridNodeApplicationService implements ManageSmokePlumePm25ConcentrationGridNodeUseCase {

    private final SmokePlumePm25ConcentrationGridNodeRepositoryPort repositoryPort;

    public SmokePlumePm25ConcentrationGridNodeApplicationService(SmokePlumePm25ConcentrationGridNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SmokePlumePm25ConcentrationGridNode createSmokePlumePm25ConcentrationGridNode(String tenantId, String title, double value) {
        SmokePlumePm25ConcentrationGridNode entity = new SmokePlumePm25ConcentrationGridNode(
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
    public Optional<SmokePlumePm25ConcentrationGridNode> findSmokePlumePm25ConcentrationGridNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SmokePlumePm25ConcentrationGridNode processOptimization(String id, String tenantId) {
        SmokePlumePm25ConcentrationGridNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SmokePlumePm25ConcentrationGridNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
