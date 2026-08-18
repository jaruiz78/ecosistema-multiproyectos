package com.corp.proyectoinclusiveaccessibletourism.application.service;

import com.corp.proyectoinclusiveaccessibletourism.domain.model.AccessiblePoiNode;
import com.corp.proyectoinclusiveaccessibletourism.domain.port.in.ManageAccessiblePoiNodeUseCase;
import com.corp.proyectoinclusiveaccessibletourism.domain.port.out.AccessiblePoiNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AccessiblePoiNode.
 */
@Service
public class AccessiblePoiNodeApplicationService implements ManageAccessiblePoiNodeUseCase {

    private final AccessiblePoiNodeRepositoryPort repositoryPort;

    public AccessiblePoiNodeApplicationService(AccessiblePoiNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AccessiblePoiNode createAccessiblePoiNode(String tenantId, String title, double value) {
        AccessiblePoiNode entity = new AccessiblePoiNode(
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
    public Optional<AccessiblePoiNode> findAccessiblePoiNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AccessiblePoiNode processOptimization(String id, String tenantId) {
        AccessiblePoiNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AccessiblePoiNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
