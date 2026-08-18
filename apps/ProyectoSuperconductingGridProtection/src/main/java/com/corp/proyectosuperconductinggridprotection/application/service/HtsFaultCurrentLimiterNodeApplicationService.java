package com.corp.proyectosuperconductinggridprotection.application.service;

import com.corp.proyectosuperconductinggridprotection.domain.model.HtsFaultCurrentLimiterNode;
import com.corp.proyectosuperconductinggridprotection.domain.port.in.ManageHtsFaultCurrentLimiterNodeUseCase;
import com.corp.proyectosuperconductinggridprotection.domain.port.out.HtsFaultCurrentLimiterNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HtsFaultCurrentLimiterNode.
 */
@Service
public class HtsFaultCurrentLimiterNodeApplicationService implements ManageHtsFaultCurrentLimiterNodeUseCase {

    private final HtsFaultCurrentLimiterNodeRepositoryPort repositoryPort;

    public HtsFaultCurrentLimiterNodeApplicationService(HtsFaultCurrentLimiterNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HtsFaultCurrentLimiterNode createHtsFaultCurrentLimiterNode(String tenantId, String title, double value) {
        HtsFaultCurrentLimiterNode entity = new HtsFaultCurrentLimiterNode(
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
    public Optional<HtsFaultCurrentLimiterNode> findHtsFaultCurrentLimiterNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HtsFaultCurrentLimiterNode processOptimization(String id, String tenantId) {
        HtsFaultCurrentLimiterNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HtsFaultCurrentLimiterNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
