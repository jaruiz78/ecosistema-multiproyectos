package com.corp.proyectoexosomenanovesicletherapeutics.application.service;

import com.corp.proyectoexosomenanovesicletherapeutics.domain.model.ExosomeSurfaceMarkerTropismNode;
import com.corp.proyectoexosomenanovesicletherapeutics.domain.port.in.ManageExosomeSurfaceMarkerTropismNodeUseCase;
import com.corp.proyectoexosomenanovesicletherapeutics.domain.port.out.ExosomeSurfaceMarkerTropismNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ExosomeSurfaceMarkerTropismNode.
 */
@Service
public class ExosomeSurfaceMarkerTropismNodeApplicationService implements ManageExosomeSurfaceMarkerTropismNodeUseCase {

    private final ExosomeSurfaceMarkerTropismNodeRepositoryPort repositoryPort;

    public ExosomeSurfaceMarkerTropismNodeApplicationService(ExosomeSurfaceMarkerTropismNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ExosomeSurfaceMarkerTropismNode createExosomeSurfaceMarkerTropismNode(String tenantId, String title, double value) {
        ExosomeSurfaceMarkerTropismNode entity = new ExosomeSurfaceMarkerTropismNode(
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
    public Optional<ExosomeSurfaceMarkerTropismNode> findExosomeSurfaceMarkerTropismNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ExosomeSurfaceMarkerTropismNode processOptimization(String id, String tenantId) {
        ExosomeSurfaceMarkerTropismNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ExosomeSurfaceMarkerTropismNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
