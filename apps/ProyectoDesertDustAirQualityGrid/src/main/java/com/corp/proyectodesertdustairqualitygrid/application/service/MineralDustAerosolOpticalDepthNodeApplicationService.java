package com.corp.proyectodesertdustairqualitygrid.application.service;

import com.corp.proyectodesertdustairqualitygrid.domain.model.MineralDustAerosolOpticalDepthNode;
import com.corp.proyectodesertdustairqualitygrid.domain.port.in.ManageMineralDustAerosolOpticalDepthNodeUseCase;
import com.corp.proyectodesertdustairqualitygrid.domain.port.out.MineralDustAerosolOpticalDepthNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MineralDustAerosolOpticalDepthNode.
 */
@Service
public class MineralDustAerosolOpticalDepthNodeApplicationService implements ManageMineralDustAerosolOpticalDepthNodeUseCase {

    private final MineralDustAerosolOpticalDepthNodeRepositoryPort repositoryPort;

    public MineralDustAerosolOpticalDepthNodeApplicationService(MineralDustAerosolOpticalDepthNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MineralDustAerosolOpticalDepthNode createMineralDustAerosolOpticalDepthNode(String tenantId, String title, double value) {
        MineralDustAerosolOpticalDepthNode entity = new MineralDustAerosolOpticalDepthNode(
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
    public Optional<MineralDustAerosolOpticalDepthNode> findMineralDustAerosolOpticalDepthNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MineralDustAerosolOpticalDepthNode processOptimization(String id, String tenantId) {
        MineralDustAerosolOpticalDepthNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MineralDustAerosolOpticalDepthNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
