package com.corp.proyectohyporheicwaterpurification.application.service;

import com.corp.proyectohyporheicwaterpurification.domain.model.HyporheicNitrateDenitrificationNode;
import com.corp.proyectohyporheicwaterpurification.domain.port.in.ManageHyporheicNitrateDenitrificationNodeUseCase;
import com.corp.proyectohyporheicwaterpurification.domain.port.out.HyporheicNitrateDenitrificationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HyporheicNitrateDenitrificationNode.
 */
@Service
public class HyporheicNitrateDenitrificationNodeApplicationService implements ManageHyporheicNitrateDenitrificationNodeUseCase {

    private final HyporheicNitrateDenitrificationNodeRepositoryPort repositoryPort;

    public HyporheicNitrateDenitrificationNodeApplicationService(HyporheicNitrateDenitrificationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HyporheicNitrateDenitrificationNode createHyporheicNitrateDenitrificationNode(String tenantId, String title, double value) {
        HyporheicNitrateDenitrificationNode entity = new HyporheicNitrateDenitrificationNode(
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
    public Optional<HyporheicNitrateDenitrificationNode> findHyporheicNitrateDenitrificationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HyporheicNitrateDenitrificationNode processOptimization(String id, String tenantId) {
        HyporheicNitrateDenitrificationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HyporheicNitrateDenitrificationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
