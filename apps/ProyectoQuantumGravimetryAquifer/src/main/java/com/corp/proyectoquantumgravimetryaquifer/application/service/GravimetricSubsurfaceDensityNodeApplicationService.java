package com.corp.proyectoquantumgravimetryaquifer.application.service;

import com.corp.proyectoquantumgravimetryaquifer.domain.model.GravimetricSubsurfaceDensityNode;
import com.corp.proyectoquantumgravimetryaquifer.domain.port.in.ManageGravimetricSubsurfaceDensityNodeUseCase;
import com.corp.proyectoquantumgravimetryaquifer.domain.port.out.GravimetricSubsurfaceDensityNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GravimetricSubsurfaceDensityNode.
 */
@Service
public class GravimetricSubsurfaceDensityNodeApplicationService implements ManageGravimetricSubsurfaceDensityNodeUseCase {

    private final GravimetricSubsurfaceDensityNodeRepositoryPort repositoryPort;

    public GravimetricSubsurfaceDensityNodeApplicationService(GravimetricSubsurfaceDensityNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GravimetricSubsurfaceDensityNode createGravimetricSubsurfaceDensityNode(String tenantId, String title, double value) {
        GravimetricSubsurfaceDensityNode entity = new GravimetricSubsurfaceDensityNode(
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
    public Optional<GravimetricSubsurfaceDensityNode> findGravimetricSubsurfaceDensityNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GravimetricSubsurfaceDensityNode processOptimization(String id, String tenantId) {
        GravimetricSubsurfaceDensityNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GravimetricSubsurfaceDensityNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
