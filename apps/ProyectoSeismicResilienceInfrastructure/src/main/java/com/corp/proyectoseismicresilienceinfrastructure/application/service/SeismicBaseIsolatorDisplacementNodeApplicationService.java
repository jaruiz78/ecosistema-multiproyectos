package com.corp.proyectoseismicresilienceinfrastructure.application.service;

import com.corp.proyectoseismicresilienceinfrastructure.domain.model.SeismicBaseIsolatorDisplacementNode;
import com.corp.proyectoseismicresilienceinfrastructure.domain.port.in.ManageSeismicBaseIsolatorDisplacementNodeUseCase;
import com.corp.proyectoseismicresilienceinfrastructure.domain.port.out.SeismicBaseIsolatorDisplacementNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SeismicBaseIsolatorDisplacementNode.
 */
@Service
public class SeismicBaseIsolatorDisplacementNodeApplicationService implements ManageSeismicBaseIsolatorDisplacementNodeUseCase {

    private final SeismicBaseIsolatorDisplacementNodeRepositoryPort repositoryPort;

    public SeismicBaseIsolatorDisplacementNodeApplicationService(SeismicBaseIsolatorDisplacementNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SeismicBaseIsolatorDisplacementNode createSeismicBaseIsolatorDisplacementNode(String tenantId, String title, double value) {
        SeismicBaseIsolatorDisplacementNode entity = new SeismicBaseIsolatorDisplacementNode(
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
    public Optional<SeismicBaseIsolatorDisplacementNode> findSeismicBaseIsolatorDisplacementNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SeismicBaseIsolatorDisplacementNode processOptimization(String id, String tenantId) {
        SeismicBaseIsolatorDisplacementNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SeismicBaseIsolatorDisplacementNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
